package de.nordgedanken.auto_hotkey.psi;

import com.intellij.psi.tree.IElementType;
import de.nordgedanken.auto_hotkey.lang.core.AhkLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class AHKElementType  extends IElementType {
    public AHKElementType( @NotNull @NonNls String debugName) {
        super(debugName, AhkLanguage.INSTANCE);
    }
}
