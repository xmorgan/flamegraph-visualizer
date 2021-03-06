package com.github.kornilova_l.flamegraph.plugin.server.trees.util.accumulative_trees.back_traces;

import com.github.kornilova_l.flamegraph.plugin.server.tree_request_handlers.tree.TreeRequestHandlerKt;
import com.github.kornilova_l.flamegraph.plugin.server.trees.TreeBuilder;
import com.github.kornilova_l.flamegraph.plugin.server.trees.util.TreesUtil;
import com.github.kornilova_l.flamegraph.proto.TreeProtos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used only if calltraces tree contains less than {@link TreeRequestHandlerKt#maximumNodesCount}
 */
public final class BackTracesBuilder implements TreeBuilder {
    private TreeProtos.Tree.Builder treeBuilder;
    @Nullable
    private final TreeProtos.Tree tree;
    private int maxDepth = 0;

    public BackTracesBuilder(@NotNull TreeProtos.Tree outgoingCalls) {
        if (outgoingCalls.getTreeInfo().getNodesCount() > TreeRequestHandlerKt.maximumNodesCount) {
            throw new IllegalArgumentException("Calltraces must contain less than " + TreeRequestHandlerKt.maximumNodesCount + " nodes");
        }
        initTreeBuilder();
        for (TreeProtos.Tree.Node node : outgoingCalls.getBaseNode().getNodesList()) {
            traverseTree(node, 0);
        }
        if (treeBuilder.getBaseNode().getNodesCount() == 0) {
            tree = null;
            return;
        }
        TreesUtil.INSTANCE.setNodesOffsetRecursively(treeBuilder.getBaseNodeBuilder(), 0);
        TreesUtil.INSTANCE.setTreeWidth(treeBuilder);
        TreesUtil.INSTANCE.setNodesCount(treeBuilder);
        treeBuilder.setDepth(maxDepth);
        tree = treeBuilder.build();
    }

    @Nullable
    public TreeProtos.Tree getTree() {
        return tree;
    }

    private void initTreeBuilder() {
        treeBuilder = TreeProtos.Tree.newBuilder()
                .setBaseNode(TreeProtos.Tree.Node.newBuilder());
    }

    private static class NodeBuilderAndTime {
        TreeProtos.Tree.Node.Builder nodeBuilder;
        long time;

        NodeBuilderAndTime(TreeProtos.Tree.Node.Builder nodeBuilder,
                           long time) {
            this.time = time;
            this.nodeBuilder = nodeBuilder;
        }
    }

    private List<NodeBuilderAndTime> traverseTree(TreeProtos.Tree.Node node, int depth) {
        depth++;
        if (depth > maxDepth) {
            maxDepth = depth;
        }
        if (node.getNodesCount() == 0) { // leaf
            return addLeafToBaseNodeChildren(node);
        } else {
            ArrayList<NodeBuilderAndTime> arrayList = new ArrayList<>();
            for (TreeProtos.Tree.Node childNode : node.getNodesList()) {
                for (NodeBuilderAndTime returnedNodeBuilder : traverseTree(childNode, depth)) {
                    long time = returnedNodeBuilder.time;
                    TreeProtos.Tree.Node.Builder childOfReturnedNode =
                            TreesUtil.INSTANCE.updateNodeList(returnedNodeBuilder.nodeBuilder, node.getNodeInfo(), time);
                    arrayList.add(
                            new NodeBuilderAndTime(
                                    childOfReturnedNode,
                                    time
                            )
                    );
                }
            }
            return arrayList;
        }
    }

    private List<NodeBuilderAndTime> addLeafToBaseNodeChildren(TreeProtos.Tree.Node node) {
        ArrayList<NodeBuilderAndTime> arrayList = new ArrayList<>();
        TreeProtos.Tree.Node.Builder newNode =
                TreesUtil.INSTANCE.updateNodeList(treeBuilder.getBaseNodeBuilder(), node.getNodeInfo(), node.getWidth());
        arrayList.add(
                new NodeBuilderAndTime(
                        newNode,
                        node.getWidth()
                )
        );
        return arrayList;
    }
}
