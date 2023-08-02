# 查看文件报错最大的大小
show variables like '%max_allowed_packet%';

# 修改配置，即是扩大配置限制，将4M增大，这里我将其修改为50M,命令如下
# set global max_allowed_packet = 5*1024*1024*10;