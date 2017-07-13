package com.github.kornilova_l.flamegraph.plugin.server;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.ide.BuiltInServerManager;

public class ShowProfilingResultsAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        BrowserUtil.browse("http://localhost:" +
                BuiltInServerManager.getInstance().getPort() +
                ServerNames.SELECT_FILE);
    }
}