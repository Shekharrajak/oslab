#include <unistd.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

#define LOOP 20
#define SIZE 10
#define READERS 2
#define WRITERS 1

pthread_mutex_t mut;
pthread_cond_t can_read,can_write;


volatile int item=0;
volatile int writer_content=1;
volatile int nactive=0; // >0-->no. of active readers||||==-1-->one active writer
volatile int npreaders=0;
volatile int npwriters=0;

void* writer(void *args)
{
	int i;
	for(i=0;i<LOOP;i++)
	{
		pthread_mutex_lock(&mut);
		npwriters++;
		if(npreaders>0)
			pthread_cond_wait(&can_write,&mut);

		while(nactive!=0)
		{
			printf("writer no %d waiting to write\n",*(int *)args);
			pthread_cond_wait(&can_write,&mut);
		} 
		nactive=-1;
		npwriters--;
		pthread_mutex_unlock(&mut);


		item=writer_content++;
		printf("writer no %d wrote %d\n",*(int *)args,item);

		pthread_mutex_lock(&mut);
		nactive=0;
		if(npreaders>0)
			pthread_cond_broadcast(&can_read);
		else pthread_cond_signal(&can_write);
		pthread_mutex_unlock(&mut);
	}
	return NULL;
}

void *reader(void *args)
{
	int i;
	for(i=0;i<LOOP;i++)
	{
		pthread_mutex_lock(&mut);
		npreaders++;

		if(npwriters>0)
			pthread_cond_wait(&can_read,&mut);
		while(nactive==-1)
		{
			printf("reader no %d waiting to read\n",*(int *)args);
			pthread_cond_wait(&can_read,&mut);
		}
		nactive++;
		npreaders--;
		pthread_mutex_unlock(&mut);

		printf("reader no %d read %d\n",*(int *)args,item);

		pthread_mutex_lock(&mut);
		nactive--;
		if(nactive==0)
			pthread_cond_signal(&can_write);
		pthread_mutex_unlock(&mut);
	}
}

int main()
{
	pthread_t reader_id[READERS],writer_id[WRITERS];
	int reader_no[READERS],writer_no[WRITERS];

	pthread_mutex_init(&mut,NULL);
	pthread_cond_init(&can_read,NULL);
	pthread_cond_init(&can_write,NULL);

	int i;
	for(i=0;i<READERS;i++)reader_no[i]=i+1;
	for(i=0;i<WRITERS;i++)writer_no[i]=i+1;
	for(i=0;i<READERS;i++)pthread_create(reader_id+i,NULL,reader,reader_no+i);
	for(i=0;i<WRITERS;i++)pthread_create(writer_id+i,NULL,writer,writer_no+i);;
	
	for(i=0;i<READERS;i++)pthread_join(reader_id[i],NULL);
	for(i=0;i<WRITERS;i++)pthread_join(writer_id[i],NULL);
	return;
}
