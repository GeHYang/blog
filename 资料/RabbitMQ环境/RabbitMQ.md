# 消息队列RabbitMQ

## 1、安装

* 安装环境：Linux CenterOS7
* Erlang 7
* gcc C++环境

### 1.1、安装所需环境

````shell
yum -y install make gcc gcc-c++ kernel-devel m4 ncurses-devel openssl-devel wget
````



### 1.2、安装[Erlang 7](https://packagecloud.io/rabbitmq/erlang)

````shell
# 下载Erlang
wget --content-disposition https://packagecloud.io/rabbitmq/erlang/packages/el/7/erlang-23.3.4.11-1.el7.x86_64.rpm/download.rpm
# 解压
rpm -Uvh erlang-23.3.4.11-1.el7.x86_64.rpm
# 安装
yum install -y erlang
# 查看版本
erl -v
````

### 1.3、安装RabbitMQ

* 命令下载

````shell
wget https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.9.13/rabbitmq-server-3.9.13-1.el7.noarch.rpm
````

* 直接下载

https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.9.13/rabbitmq-server-3.9.13-1.el7.noarch.rpm

#### 1.3.1、安装RabbitMQ

````shell
# 解压
rpm -Uvh rabbitmq-server-3.9.13-1.el8.noarch.rpm
# 安装
yum install -y rabbitmq-server

# 启动rabbitmq
systemctl start rabbitmq-server

# 查看rabbitmq状态
systemctl status rabbitmq-server

# 安装RabbitMQWeb管理界面插件
rabbitmq-plugins enable rabbitmq_management

# 添加用户
# rabbitmqctl add_user 用户名 密码
rabbitmqctl add_user admin admin
# 设置用户角色,分配操作权限
#rabbitmqctl set_user_tags 用户名 角色
rabbitmqctl set_user_tags admin administrator
# 为用户添加资源权限(授予访问虚拟机根节点的所有权限)
rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"


# 设置rabbitmq服务开机自启动
systemctl enable rabbitmq-server

# 关闭rabbitmq服务
systemctl stop rabbitmq-server

# 重启rabbitmq服务
systemctl restart rabbitmq-server

# 修改密码
rabbitmqctl change_ password 用户名 新密码

# 删除用户
rabbitmqctl delete_user 用户名

# 查看用户清单
rabbitmqctl list_users


firewall-cmd --zone=public --add-port=5672/tcp --permanent
firewall-cmd --zone=public --add-port=15672/tcp --permanent
systemctl restart firewalld
firewall-cmd --zone=public --list-ports


````

