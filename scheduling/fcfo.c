#include <stdio.h>
int main()
{
 int n, *p,*w;
 int sum=0;
 system("clear");
 printf("Enter the number of processes:");
 scanf("%d", &n);
 p=(int*)malloc(n*sizeof(int));
 w=(int*)malloc(n*sizeof(int));
 for(int i=0; i<n; i++)
 {
  printf("Burst time for process %d:", i+1);
  scanf("%d", &p[i]);
  w[i]=sum;
  sum+=p[i];
 }
 system("clear");
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
Output:
Enter the number of processes:4
Burst time for process 1:12
Burst time for process 2:5
Burst time for process 3:8
Burst time for process 4:1

  Process  Waiting Time
  1      0
  2      12
  3      17
  4      25
Average waiting time:13 sec
*/