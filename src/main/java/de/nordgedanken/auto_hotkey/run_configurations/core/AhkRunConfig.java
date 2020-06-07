package de.nordgedanken.auto_hotkey.run_configurations.core;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ui.configuration.IdeaProjectSettingsService;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import de.nordgedanken.auto_hotkey.AhkPluginConstants;
import de.nordgedanken.auto_hotkey.run_configurations.execution.AhkRunState;
import de.nordgedanken.auto_hotkey.run_configurations.ui.AhkRunConfigSettingsEditor;
import de.nordgedanken.auto_hotkey.sdk.AhkSdkType;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines instances of Ahk run configurations.
 */
@State(
	name = AhkPluginConstants.PLUGIN_NAME,
	storages = {@Storage(AhkPluginConstants.PLUGIN_NAME + "__run-configuration.xml")}
)
public class AhkRunConfig extends RunConfigurationBase<Object> {
	public static final String KEY_SCRIPTPATH = AhkPluginConstants.PLUGIN_NAME + "scriptPath";
	public static final String KEY_ARGUMENTS = AhkPluginConstants.PLUGIN_NAME + "arguments";
	private String pathToScript;
	private String arguments;

	protected AhkRunConfig(@NotNull Project project, @Nullable ConfigurationFactory factory, @Nullable String name) {
		super(project, factory, name);
	}

	@Override
	public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
		return new AhkRunConfigSettingsEditor(this.getProject());
	}

	@Override
	public @Nullable RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
		Sdk projectSDK = ProjectRootManager.getInstance(getProject()).getProjectSdk();
		//validating the Ahk SDK here. Eventually this should be removed and validation should happen while editing the run config
		if(projectSDK == null || !(projectSDK.getSdkType() instanceof AhkSdkType)) {
			String fullMessage = "SDK Error: You must create an AutoHotkey SDK and select it as the project's default SDK in order to run the AHK script. <br>Otherwise IntelliJ does not know what to use to run your script";
			NotificationUtil.showErrorPopup("Execution Error", fullMessage, getProject(), environment);
			IdeaProjectSettingsService.getInstance(getProject()).openProjectSettings();
		} else if(pathToScript.isEmpty()) {
			String fullMessage = "Error: You must specify the path to the script you want to execute within the run config";
			NotificationUtil.showErrorPopup("Execution Error", fullMessage, getProject(), environment);
		} else {
			//return execution runstate only if everything is good. Else return null so nothing happens
			return new AhkRunState(this, environment);
		}
		return null;
	}

	/**
	 * This READS any prior persisted configuration from the State/Storage defined by this classes annotations.
	 */
	@Override
	public void readExternal(@NotNull Element element) {
		super.readExternal(element);
		pathToScript = JDOMExternalizerUtil.readField(element, KEY_SCRIPTPATH);
		arguments = JDOMExternalizerUtil.readField(element, KEY_ARGUMENTS);
	}

	/**
	 * This WRITES/persists configurations TO the State/Storage defined by this classes annotations.
	 */
	@Override
	public void writeExternal(@NotNull Element element) {
		super.writeExternal(element);
		JDOMExternalizerUtil.writeField(element, KEY_SCRIPTPATH, pathToScript);
		JDOMExternalizerUtil.writeField(element, KEY_ARGUMENTS, arguments);
	}

	public String getPathToScript() {
		return pathToScript;
	}

	public void setPathToScript(String pathToScript) {
		this.pathToScript = pathToScript;
	}

	public String getArguments() {
		return arguments;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		return "AhkRunConfig{" + "pathToScript='" + pathToScript + '\'' + ", arguments='" + getArguments() + '\'' + '}';
	}
}
