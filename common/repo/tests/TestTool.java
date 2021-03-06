/*
 * Copyright 2018 The StartupOS Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.startupos.common.repo.tests;

import com.google.startupos.common.CommonModule;
import com.google.startupos.common.repo.GitRepoFactory;
import com.google.startupos.common.repo.Protos.Commit;
import com.google.startupos.common.repo.Protos.File;
import com.google.startupos.common.repo.Repo;
import dagger.Component;
import javax.inject.Inject;
import javax.inject.Singleton;

/** Test tool for GitRepo. */
@Singleton
public class TestTool {
  private final GitRepoFactory repoFactory;

  @Inject
  TestTool(GitRepoFactory repoFactory) {
    this.repoFactory = repoFactory;
  }

  void run(String[] args) throws Exception {
    if (args.length > 0) {
      String command = args[0];
      Repo repo = repoFactory.create(System.getenv("BUILD_WORKSPACE_DIRECTORY"));

      if (command.equals("switchBranch")) {
        String branch = args[1];
        repo.switchBranch(branch);
      } else if (command.equals("getCommits")) {
        String branch = args[1];
        for (Commit commit : repo.getCommits(branch)) {
          System.out.println();
          System.out.println(commit);
        }
      } else if (command.equals("getFilesInCommit")) {
        String commitId = args[1];
        for (File file : repo.getFilesInCommit(commitId)) {
          System.out.println();
          System.out.println(file);
        }
      } else if (command.equals("getUncommittedFiles")) {
        for (File file : repo.getUncommittedFiles()) {
          System.out.println(file);
        }
      } else if (command.equals("merge")) {
        String branch = args[1];
        repo.merge(branch);
      } else if (command.equals("isMerged")) {
        String branch = args[1];
        repo.isMerged(branch);
      } else if (command.equals("removeBranch")) {
        String branch = args[1];
        repo.removeBranch(branch);
      } else if (command.equals("listBranches")) {
        for (String branch : repo.listBranches()) {
          System.out.println(branch);
        }
      } else if (command.equals("getTextDiff")) {
        File file1 =
            File.newBuilder().setCommitId(args[1]).setFilename("tools/aa/AaModule.java").build();
        File file2 =
            File.newBuilder().setCommitId(args[2]).setFilename("tools/aa/AaModule.java").build();
        System.out.println(repo.getTextDiff(file1, file2));
      } else if (command.equals("getFileContents")) {
        System.out.println(repo.getFileContents(args[1], args[2]));
      } else {
        System.out.println("Please specify command");
      }
    }
  }

  @Singleton
  @Component(modules = CommonModule.class)
  interface TestToolComponent {
    TestTool getTestTool();
  }

  public static void main(String[] args) throws Exception {
    DaggerTestTool_TestToolComponent.create().getTestTool().run(args);
  }
}

