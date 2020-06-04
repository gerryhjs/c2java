//Client
#include<stdio.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<unistd.h>
#include<arpa/inet.h>
#include <string.h>
#include <stdlib.h>

#define IP "127.0.0.1"
#define packSize 10000
#define maxSize 10000
int main()
{
    int sockfd;
    int len;
    struct sockaddr_in address;
    int result;
    char* ch="11111111\n";
    while(1){
        sockfd = socket(AF_INET,SOCK_STREAM,0);
        address.sin_family = AF_INET;
//        address.sin_addr.s_addr = inet_addr("127.0.0.1");
        address.sin_addr.s_addr = inet_addr("192.168.33.104");
        address.sin_port = htons(20000);
        len = sizeof(address);
        result = connect(sockfd,(struct sockaddr *)&address,len);
        if(result == -1) {
            perror("oops:client\n");
            exit(1);
        }
        printf("请输入你要传输的数据\n");
//        gets(ch);
//        len = strlen(ch);
//        ch[len] = '\0';
        printf("%d, %s\n", len, ch);
        write(sockfd,ch,len+1);
        close(sockfd);
    }
    exit(0);
    return 0;



}
