-- 新增 相册删除状态字段 已执行
ALTER TABLE activity_album ADD delete_status int(2) COMMENT '是否删除' DEFAULT 0;

-- --------------------------------------------------------------
-- 2018-5-29 14:15:34 新增人脸库标识字段 已执行
alter table activity add column face_set_token VARCHAR(255) default null comment '人脸库标识';
alter table activity add column is_open_face_search int(1) default 0 comment '是否开启人脸识别:1开启,0关闭';
alter table activity_photo add column is_save_to_faceset int(1) default 0 comment '该图片是否存入face_set,0:未存入 1:已存入';

-- activity_photo_token表 已执行
-- 2018年5月29日15:48:40
CREATE TABLE `activity_photo_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `album_id` int(11)  not NULL comment '相册id',
  `user_id` int(11) not	NULL comment '用户id',
	`activity_id` int(11) not Null comment '活动id',
	`face_token` VARCHAR(255) DEFAULT NULL comment '人脸标识',
	`photo_id` int(11) not	NULL comment '图片id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

-- 2018年6月13日11:45:12新增字段 已执行
alter table activity_statistics add column real_participants_num int(8) default 0 comment '真实参与人数';
alter table activity_statistics add column real_comment_num int(8)  default 0 comment '真实评论数';
alter table activity_statistics add column real_share_num int(8)  default 0 comment '真实分享数';
alter table activity_statistics add column real_like_num int(8)  default 0 comment '真实点赞数';
alter table activity modify column is_open_face_search int(11) DEFAULT '0' COMMENT '1:开启人脸识别,最大传回5张, 0:未开启 2:开启人脸识别,尽可能多的传回照片';
alter table activity_photo_token add column same_person varchar(255) default null comment '如果值相同,则为同一个人';

-- 2018年6月28日 世界杯活动增加 推广人字段
alter table contribute add column f_user_id int(11) comment '推广人';
alter table activity add column expect_like_num int(11) default 1000 comment '期望点赞数-默认1000'
alter table activity add column expect_participate_num int(11) default 500 comment '期望参与人数-默认200';
