module.exports = async ({ github, context, core }) => {
  const response = await github.rest.repos.listPullRequestsAssociatedWithCommit({
    owner: context.repo.owner,
    repo: context.repo.repo,
    commit_sha: context.sha,
  });

  const pr = response.data[0];
  if (!pr) {
    core.setFailed("No PR associated with this commit");
    return;
  }

  const labels = pr.labels.map(label => label.name);
  console.log("Labels on PR:", labels);

  core.setOutput("has_label", labels.includes("Release Internal"));
  core.setOutput("pr_number", pr.number);
};
