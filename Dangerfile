github.dismiss_out_of_range_messages

# Warn for large PRs to encourage smaller, more focused changes
warn("Large PR detected, consider splitting it into smaller ones!") if git.lines_of_code > 250

# Warn if the PR title contains [WIP]
warn("This PR is marked as Work in Progress, do not merge!") if github.pr_title.include?("[WIP]")

# Warn if there are merge conflicts that need to be resolved
fail("This PR has unresolved merge conflicts. Please resolve them.") if git.modified_files.any? { |file| file.include?('<<<<<<') }

# Ensure a PR description exists
fail("Please provide a meaningful description for the PR.") if github.pr_body.nil? || github.pr_body.strip.empty?

# Ensure that unit tests are updated or added for the changes made
test_files = git.modified_files.grep(%r{src/test/.*\.kt$})
implementation_files = git.modified_files.grep(%r{src/main/.*\.kt$})

if implementation_files.any? && test_files.empty?
  warn("The following implementation files were modified, but no corresponding test files were updated or added. Please consider adding or updating tests:\n\n" +
       implementation_files.map { |file| "- #{file}" }.join("\n"))
end

# Android lint integration
android_lint.report_file = "app/build/reports/lint-results-debug.xml"
android_lint.lint

# Fail if there are any TODOs in the modified code and list their file paths and line numbers
todo_locations = []

git.modified_files.each do |file|
  next unless File.exist?(file) # Ensure the file exists before reading

  File.readlines(file).each_with_index do |line, index|
    if line.include?("TODO")
      todo_locations << { file: file, line: index + 1, content: line.strip }
    end
  end
end

if todo_locations.any?
  message = "This PR contains TODO comments. Please address them:\n\n" +
            todo_locations.map { |todo| "- #{todo[:file]}:#{todo[:line]} - #{todo[:content]}" }.join("\n")
  fail(message)
end


# Warn and list which build.gradle or build.gradle.kts files were modified
modified_build_files = git.modified_files.select { |file| file.include?('build.gradle') || file.include?('build.gradle.kts') }

if modified_build_files.any?
  warn("Dependencies were modified in the following files. Ensure they are necessary and properly tested:\n\n" +
       modified_build_files.map { |file| "- #{file}" }.join("\n"))
end
