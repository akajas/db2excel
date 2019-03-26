# 数据库查询的表生成excel

适合单位日报表，周报表，或查询比较耗时的语句。配合mail自动发送，减少劳动力。

感谢 https://github.com/webinglin/excelExportor/  excel生成的代码借用这位大神。

## 说明

- 项目采用Springboot，打包成jar包，可做成定时任务。

- 采用Spring的jdbcTemplate，对查询的复杂sql比较方便。

- 有个查询Book表，做为示例。

  
