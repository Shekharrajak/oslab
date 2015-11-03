#include <stdio.h>
struct proc
{
 int b_time; //burst time
 int a_time; //arrival time
};
struct proc *p;
int n;
int give_min_idx(int upto)
{
 int min=1000, idx=-1;
 for(int i=0; i<n; i++)
 {
  if(p[i].b_time!=-1 && p[i].a_time<=upto && min>p[i].b_time)
  {
   min=p[i].b_time;
   idx=i;
  }
 }
 return idx;
}
int main()
{
 int *w;
 int time=0;
 int sum=0;
 system("clear");
 printf("Enter the number of processes:");
 scanf("%d", &n);
 p=(struct proc*)malloc(n*sizeof(struct proc));
 w=(int*)calloc(n,sizeof(int));
 for(int i=0; i<n; i++)
 {
  printf("Burst & arrival time for process %d:", i+1);
  scanf("%d%d", &p[i].b_time, &p[i].a_time);
 }
 system("clear");
 int idx;
 while((idx=give_min_idx(time))!=-1)
 {
  p[idx].b_time--;
  if(p[idx].b_time==0)
  p[idx].b_time--;
  time++;
  for(int i=0; i<n; i++)
  {
   if(p[i].b_time!=-1 && i!=idx && p[i].a_time<time)
   w[i]++;
  }
 }
 sum=0;
 printf("\t\tProcess\t\tWaiting Time\n");
 for(int i=0; i<n; i++){
 printf("\t\t%d\t\t%d\n", i+1, w[i]);
 sum+=w[i];
 }
 printf("Average waiting time:%d sec\n", sum/n);
 return 0;
}
/*
output:
Enter the number of processes:4
Burst & arrival time for process 1:8 0
Burst & arrival time for process 2:4 1
Burst & arrival time for process 3:9 2
Burst & arrival time for process 4:5 3

  Process  Waiting Time
  1      9
  2      0
  3      15
  4      2
Average waiting time:6 sec
*/