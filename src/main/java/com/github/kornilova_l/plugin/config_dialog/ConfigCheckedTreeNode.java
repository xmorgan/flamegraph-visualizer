package com.github.kornilova_l.plugin.config_dialog;

import com.intellij.ui.CheckedTreeNode;
import org.jetbrains.annotations.NotNull;

final class ConfigCheckedTreeNode extends CheckedTreeNode {
    private final String name;

    ConfigCheckedTreeNode(@NotNull String name) {
        super(name);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}