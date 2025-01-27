github.dismiss_out_of_range_messages

warn("Large PR") if git.lines_of_code > 5

warn("This PR is a Work in Progress, do not merge!") if github.pr_title.include? "[WIP]"

android_lint.report_file = "app/build/reports/lint-results-debug.xml"
android_lint.lint