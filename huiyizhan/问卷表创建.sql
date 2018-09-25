#创建 问卷调查表
# create table IF NOT EXISTS survey(
	id int(11) PRIMARY KEY COMMENT 'id',
	title VARCHAR(64) COMMENT '问卷标题',
	description VARCHAR(256) COMMENT '问卷描述(可以是富文本)',
	status VARCHAR(2) COMMENT '问卷状态(0:关闭,1:开启,2:结束）',
	dead_line datetime COMMENT '问卷截止时间',
	create_time datetime COMMENT '问卷创建时间',
	type int(2) COMMENT '问卷类型(0:问卷,1:投票)',
	uid int(11) COMMENT '所属用户' NOT NULL,
	activity_id int(11) COMMENT '所属活动id'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;