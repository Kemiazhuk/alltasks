package com.epam.rd.autocode.observer.git;

public class GitRepoObservers {

    public static Repository newRepository(){
        return new NewRepository();
    }

    public static WebHook mergeToBranchWebHook(String branchName){
        return new MergeWebHook(branchName);
    }

    public static WebHook commitToBranchWebHook(String branchName){

        return new CommitWebHook(branchName);
    }
}
