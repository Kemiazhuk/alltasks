package com.epam.rd.autocode.observer.git;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewRepository implements Repository {
    List<WebHook> webHooks = new ArrayList<>();
    Map<String, List<Commit>> branchCommits = new HashMap<>();

    public NewRepository() {
        branchCommits.put("master", new ArrayList<>());
    }

    @Override
    public void addWebHook(WebHook webHook) {
        webHooks.add(webHook);
    }

    @Override
    public Commit commit(String branch, String author, String[] changes) {
        Commit commit = new Commit(author, changes);
        List<Commit> commits = new ArrayList<>();
        commits.add(commit);

        if (!branchCommits.containsKey(branch)) {
            branchCommits.put(branch, new ArrayList<>());
        }
        branchCommits.get(branch).add(commit);
        sendEvent(branch, commits, Event.Type.COMMIT);

        return commit;
    }

    @Override
    public void merge(String sourceBranch, String targetBranch) {
        List<Commit> sourceBranchCommits = branchCommits.get(sourceBranch);
        List<Commit> targetBranchCommits = branchCommits.get(targetBranch);
        sourceBranchCommits.removeAll(targetBranchCommits);
        branchCommits.put(targetBranch, new ArrayList<>(sourceBranchCommits));
        if (!sourceBranchCommits.isEmpty()) {
            sendEvent(targetBranch, sourceBranchCommits, Event.Type.MERGE);
        }
    }

    public void sendEvent(String branch, List<Commit> commits, Event.Type type) {
        for (WebHook web : webHooks) {
            if (web.branch().equals(branch) && web.type().equals(type)) {
                Event event = new Event(type, branch, commits);
                web.onEvent(event);
            }
        }
    }
}
