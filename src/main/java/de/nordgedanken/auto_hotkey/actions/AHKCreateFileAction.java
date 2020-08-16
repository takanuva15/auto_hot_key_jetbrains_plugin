package de.nordgedanken.auto_hotkey.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import de.nordgedanken.auto_hotkey.util.AhkIcons;
import org.jetbrains.annotations.NotNull;

public class AHKCreateFileAction extends CreateFileFromTemplateAction implements DumbAware {
    private final static String CAPTION = "AutoHotKey File";

    public AHKCreateFileAction() {
        super(CAPTION, "", AhkIcons.FILE);
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle(CAPTION).addKind("Empty File", AhkIcons.FILE, "AutoHotkey File");
    }

    @Override
    protected String getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
        return CAPTION;
    }

    @Override
    protected boolean isAvailable(DataContext dataContext) {
        return super.isAvailable(dataContext);
    }
}
