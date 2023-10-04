import pika

import main

# 设置消息队列参数并连接
credentials = pika.PlainCredentials('Test', 'Test')
connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='1.13.250.164', port=5672, virtual_host='/Test', credentials=credentials))
channel = connection.channel()
# 指定一个接收队列
channel.queue_declare(queue='queue_1', durable=True)


# 远程要调用的方法
def res(message):
    return main.pdfanalysis(message)


# 接受并响应
def on_request(ch, method, props, body):
    # 得到传递过来的字符
    request = str(body.decode())
    print("请求内容：{}".format(request))
    # 调用方法
    response = res(request)
    # 再将调用的结果返回给客户端
    ch.basic_publish(exchange='rpc_exchange',
                     routing_key=props.reply_to,  # 获取要发送的队列
                     properties=pika.BasicProperties(correlation_id=props.correlation_id, delivery_mode=2),
                     # 将接收到的id发送过去
                     body=str(response)  # 将方法返回的结果返回给客户端
                     )
    print(response)


# 表示开启一个进程工作
# channel.basic_qos(prefetch_count=1)
# 接收客户端发来的消息，并使用了接收队列
channel.basic_consume('queue_1', on_request, True)
print("等待消息队列")
# 开始消费
channel.start_consuming()
