#include <unistd.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

#define SIZE 10
#define PRODUCER_SLEEP 2000000
#define CONSUMER_SLEEP 2000000 


int buff[SIZE];
volatile int head=0,tail=0;
volatile int count=0;
volatile int item=1;

pthread_mutex_t mut;
pthread_cond_t not_full,not_empty;

void* producer(void *args)
{
	while(1)
	{
		pthread_mutex_lock(&mut);
		while(count==SIZE)
		{
			printf("Producer waiting\n");
			pthread_cond_wait(&not_full,&mut);
		}
		printf("Item produced %d\n: ",item);
		buff[tail]=item++;
		count++;
		tail=(tail+1)%SIZE;
		pthread_mutex_unlock(&mut);
		pthread_cond_signal(&not_empty);

		//usleep(PRODUCER_SLEEP);
	}
	return NULL;
}

void* consumer(void* args)
{
	int it;
	while(1)
	{
		pthread_mutex_lock(&mut);
		while(count==0)
		{
			printf("consumer waiting\n");
			pthread_cond_wait(&not_empty,&mut);
		}
		it=buff[head];
		head=(head+1)%SIZE;
		count--;
		pthread_mutex_unlock(&mut);
		pthread_cond_signal(&not_full);
		printf("Item consumed %d\n: ",it);

		usleep(CONSUMER_SLEEP);
	}
	return NULL;
}

int main()
{
	pthread_t producer_id,consumer_id;

	pthread_mutex_init(&mut,NULL);
	pthread_cond_init(&not_full,NULL);
	pthread_cond_init(&not_empty,NULL);

	pthread_create(&producer_id,NULL,producer,NULL);
	pthread_create(&consumer_id,NULL,consumer,NULL);

	pthread_join(producer_id,NULL);
	pthread_join(consumer_id,NULL);
	return 0;
}