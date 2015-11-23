#include <unistd.h>
#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>

#define SIZE 10
#define BARBER_SLEEP 2000000
#define CUSTOMER_SLEEP 2000000


int queue[SIZE];
volatile int head=0,tail=0;
volatile int count=0;
volatile int customer_no=1;

pthread_cond_t not_full,not_empty;
pthread_mutex_t mut;

void* barber(void *args)
{
	while(1)
	{
		pthread_mutex_lock(&mut);
		while(count==0)
		{
			printf("barber_sleeping\n");
			pthread_cond_wait(&not_empty,&mut);
		}
		int it=queue[head];
		head=(head+1)%SIZE;
		count--;
		pthread_mutex_unlock(&mut);
		pthread_cond_signal(&not_full);
		printf("customer serviced :%d\n",it);
		usleep(BARBER_SLEEP);
	}
	return NULL;
}

void* customer(void *args)
{
	while(1)
	{
		pthread_mutex_lock(&mut);
		while(count==SIZE)
		{
			printf("Queue Full\n");
			pthread_cond_wait(&not_full,&mut);
		}
		printf("customer joined %d \n",customer_no);
		queue[tail]=customer_no++;
		tail=(tail+1)%SIZE;
		count++;
		pthread_mutex_unlock(&mut);
		pthread_cond_signal(&not_empty);

		usleep(CUSTOMER_SLEEP);

	}
	return NULL;
}

int main()
{
	pthread_t barber_id,customer_id;

	pthread_mutex_init(&mut,NULL);
	pthread_cond_init(&not_full,NULL);
	pthread_cond_init(&not_empty,NULL);

	pthread_create(&barber_id,NULL,barber,NULL);
	pthread_create(&customer_id,NULL,customer,NULL);

	pthread_join(barber_id,NULL);
	pthread_join(customer_id,NULL);
	return 0;
}
