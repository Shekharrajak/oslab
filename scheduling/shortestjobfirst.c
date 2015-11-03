#include <stdio.h>
int main()
{
 int n, *p,*w,*re;
 int sum=0;
 system("clear");
 printf("Enter the number of processes:");
 scanf("%d", &n);
 p=(int*)malloc(n*sizeof(int));
 w=(int*)malloc(n*sizeof(int));
 re=(int*)malloc(n*sizeof(int));
 for(int i=0; i<n; i++)
 {
  printf("Burst time for process %d:", i+1);
  scanf("%d", &p[i]);
  re[i]=i;
 }
 system("clear");
 for(int i=0; i<n; i++)
 {
  for(int j=0; j<n-1; j++)
  {
   if(p[j] > p[j+1])
   { 
    int tmp=p[j];
    p[j]=p[j+1];
    p[j+1]=tmp;
    tmp=re[j];
    re[j]=re[j+1];
    re[j+1]=tmp;
   }
  }
 }
 sum=0;
 for(int i=0; i<n; i++)
 {
  w[i]=sum;
  sum+=p[i];
 }
 printf("\t\tProcess\t\tWaiting Time\n");
 sum=0;
 int cnt=0;
 while(cnt!=n)
 {
  for(int i=0; i<n; i++)
  {
   if(cnt==re[i])
   {
    printf("\t\t%d\t\t%d\n", cnt+1, w[i]);
    sum+=w[i];
    cnt++;
   }
  }
 }
 
 printf("Average waiting time:%d sec\n", sum/n);
 return 0;
}
/*Output:
Enter the number of processes:4
Burst time for process 1:12
Burst time for process 2:5
Burst time for process 3:8
Burst time for process 4:1

  Process  Waiting Time
  1      14
  2      1
  3      6
  4      0
Average waiting time:5 sec
*/