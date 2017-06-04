insert  into `role_t`(`id`,`code`,`name`,`create_time`,`update_time`,`status`,`role_desc`) values (1,'DEVELOPER','平台开发者','2016-07-13 14:51:16','2016-04-25 11:00:40',0,'发布APP'),(2,'EMC','企业管理员','2016-07-13 14:51:03','2016-04-25 11:00:38',0,'发布APP，订阅APP，分发APP'),(3,'CMC','应用管理员','2016-07-13 14:51:12','2016-07-05 10:31:37',0,'分发APP');

insert  into `permission_mapper_t`(`id`,`role_id`,`permission_id`,`permission_code`,`permission_name`) values (1,2,1,'SUBCRIBLE','订阅'),(2,2,2,'PUBLISH','发布'),(3,2,3,'GRANT','分发'),(4,3,3,'GRANT','分发'),(5,1,2,'PUBLISH','发布');
