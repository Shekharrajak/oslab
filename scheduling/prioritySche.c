#include <stdio.h>
struct proc
{
       int b_time;
       int prio;
       };
int give_min(struct proc f[], int n)
{
    int min=1000,idx=-1;
    for(int i=0; i<n; i++)
    {
            if(f[i].prio!=-1 && f[i].prio < min)
            {
                               min=f[i].prio;
                               idx=i;
                               }
                               }
       return idx;
}                                  
int main()
{
    struct proc *g;
 int n, *p,*w;
 int sum=0;
 system("clear");
 printf("Enter the number of processes:");
 scanf("%d", &n);
 g=(struct proc*)malloc(n*sizeof(struct proc));
 w=(int*)malloc(n*sizeof(int));
 for(int i=0; i<n; i++)
 {
  printf("Burst time & priority for process %d:", i+1);
  scanf("%d%d", &g[i].b_time, &g[i].prio);
 }
 system("clear");
 sum=0;
 int idx;
 while((idx=give_min(g,n))!=-1)
 {
          w[idx]=sum;
          sum+=g[idx].b_time;
          g[idx].prio=-1;
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
/*output:
Enter the number of processes:5
Burst time & priority for process 1:10 3
Burst time & priority for process 2:1 1
Burst time & priority for process 3:2 4
Burst time & priority for process 4:1 5
Burst time & priority for process 5:5 2

                Process         Waiting Time
                1               6
                2               0
                3               16
                4               18
                5               1
Average waiting time:8 sec
*/