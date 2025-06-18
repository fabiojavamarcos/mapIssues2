Reads the file with issues and PRs linked (from [ETL2-Pipeline](https://github.com/fabiojavamarcos/ETL2-Pipeline)) and populates the pr_issue table (skills database).

## Arguments example:
Username: postgres\
Password: admin\
Database name: all_projects_without_filter_new\
Input file: /Users/fd252/OneDrive/Production/ETL2-Pipeline-main/data/output/rxjava/rxjava_df_merge.csv\
Project: rxjava\
Opation: "AS IS" // "LOWER" - every text is lower case (good for TF-IDF). "AS IS" cases as is. Templates are not removed. Any other value: removes templates comparing the characters without transform in lower cases.
