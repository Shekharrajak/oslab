#include<stdio.h>
 
 # define maxn 10  
 typedef struct banker  
 {  
  int available[maxn] ;  
  int claim[maxn][maxn] ;  
  int allocation[maxn][maxn] ;  
  int need[maxn][maxn] ;  
  int request[maxn];  
 }banker ;  
 void display(int,int,int resource[maxn],banker);  
 void bankersalgo(int nm,int resource [maxn],int rs);  
 int safe(int rs,int nm,int resource [maxn],banker) ;  
 void main()  
 {  
  int ch=0,nm=0,i,j,k,resource[maxn],request[maxn],rs ,flag=0;  
  char temp1;  
  struct banker bankers,b ;  
  //clrscr() ;  
  do  
  {  
    printf("\t\t\t\t***MAIN MENU***\n") ;  
    printf("\t\t\t\t1.ENTER MATRICES\n");  
    printf("\t\t\t\t2.DISPLAY MATRICES \n");  
    printf("\t\t\t\t3.BANKERS ALGORITM (SAFETY & ALLOCATION\n" );  
    printf("\t\t\t\t4.EXIT\n");  
    printf("\t\t\t\tENTER UR CHOICE!!\n");  
    scanf("%d",&ch);  
    switch (ch)  
    {  
    case 1:  
     //clrscr();  
     printf("ENTER NO OF PROCESS!!\n") ;  
     scanf("%d",&nm);  
     printf("ENTER NO OF RESOURCE !!\n") ;  
     scanf("%d",&rs);  
     for(i=0;i<rs;i++)     {  
       printf("ENTER RESOURCE MATRIX %d!!\n",i) ;  
       scanf("%d",&resource[i]) ;  
     }  
     printf("ENTER CLAIM MATRIX!!\n") ;  
     for(i=0;i<nm;i++)      {  
       for(j=0;j<rs;j++)        {  
         printf("\nENTER %d %d!!\n",i,j) ;  
         scanf("%d",&bankers.claim[i][j]) ;  
         if(bankers.claim[i][j]>resource[j])  
         {  
           printf(" WRONG VALUES ENTERED") ;  
           j--;  
         }  
       }  
     }  
     for(i=0;i<nm;i++)     {  
       for(j=0;j<rs;j++)       {  
         bankers.allocation[i][j]=0;  
         bankers.available[j]=resource[j];  
       }  
     }  
       //clrscr();  
       display(nm,rs,resource,bankers);  
       printf("\nENTER ALLOCATION MATRIX!!\n") ;  
       for(i=0;i<nm;i++)       {  
         for(j=0;j<rs;j++)        {  
           printf("\nENTER %d %d!!",i,j) ;  
           scanf("%d",&bankers.allocation[i][j]) ;  
           if((bankers.allocation[i][j]>bankers.claim[i][j]) && (bankers.allocation[i][j]>bankers.available[j]))  
           {  
             printf(" WRONG VALUES ENTERED") ;  
             j--;  
           }  
           else  {  
             bankers.available[j]=bankers.available[j]-bankers.allocation[i][j];  
             bankers.need[i][j]=bankers.claim[i][j]-bankers.allocation[i][j];  
             }  
         }  
         display(nm,rs,resource,bankers);  
       }  
       break ;  
    case 2:  
     display(nm,rs,resource,bankers);  
     break ;  
    case 3:  
     i=safe(rs,nm,resource,bankers) ;  
     display(nm,rs,resource,bankers);  
     do  
     {  
     if(i==1)  
     {  
       printf("\n Enter the request process no P");  
       scanf("%d",&j);  
       printf("\n Enter process %d's request",j);  
       for(k=0;k<rs;k++)      {  
         scanf("%d",&request[k]);  
         if((request[k]>bankers.need[j][k])&& (request[k]>bankers.available[k]))  
         {  
           printf("\n Not valid request");  
           k--;  
         }  
       }  
           b=bankers;  
           for(k=0;k<rs;k++)          {  
             b.allocation[j][k]+=request[k];  
             b.need[j][k]-=request[k];  
             b.available[k]-=request[k];  
           }  
           display(nm,rs,resource,b);  
           printf("\n The new state's safety is checking");  
           i=safe(rs,nm,resource,b);  
           if(i==1)  
           {    display(nm,rs,resource,b);  
             bankers=b;  
             printf(" This is a Current state ");  
           }  
           else  
           {  
             display(nm,rs,resource,bankers);  
             printf(" This is a Current safe state ");  
           }  
          printf("\nDo u want to add more request on current system(0/1)");  
          scanf("%d",&flag);  
       }  
     else  
     {  
       printf("\n Current system is unsafe state");  
       //exit(0);  
	return ;
      }  
      }while(flag==1);  
      break ;  
    default :  
      printf("ENTER RIGHT OPTION!!\n") ;  
   }  
  }while(ch!=4 );  
 }  
 void display(int nm,int rs,int resource[maxn],banker bankers)  
 {  
   int i,j;  
   //clrscr();  
   printf("Total instances of resources are:-");  
   for(i=0;i<rs;i++)    printf("\t%d",resource[i]);  
   printf("\nClaim \t Alloc \t Need");  
   for(i=0;i<nm;i++)  {  
     printf("\n");  
     for(j=0;j<rs;j++)      printf(" %d",bankers.claim[i][j]);  
     printf("\t");  
     for(j=0;j<rs;j++)      printf(" %d",bankers.allocation[i][j]);  
     printf("\t");  
     for(j=0;j<rs;j++)    {  
       bankers.need[i][j]=bankers.claim[i][j]-bankers.allocation[i][j];  
       printf(" %d",bankers.need[i][j]);  
     }  
   }  
   printf("\nCurrent avialble is :");  
   for(i=0;i<rs;i++)    printf("%d ", bankers.available[i]);  
   //getch();  
 }  
 int safe(int rs,int nm,int resource[maxn],banker bankers)  
 {  
  int array[maxn],possible=1,i=0,j=0,k=0 ,flag[10];  
  banker b=bankers;  
  for(i=0;i<nm;i++) {   array[i]=0;  
   flag[i]=0;  
  }  
  for(i=0;i<nm;i++)   {  
    if(flag[i]==0)  
    {  
     for(j=0;j<rs;j++)    {  
       if(b.need[i][j]<=b.available[j])  
       {  
         possible=0 ;  
       }  
       else  
       {  
         possible=1;  
         break;  
       }  
     }  
     if(possible==1)  
     {  
       printf("\n%d PROCESS CAN NOT PROCESSED",i) ;  
     }  
     else  
     {  
       printf("\nPROCESS%d IS PROCESSING",i) ;  
       for(j=0;j<rs;j++)      {  
         b.available[j]+=b.allocation[i][j];  
         b.allocation[i][j]=0;  
         b.claim[i][j]=0;  
       }  
       flag[i]=1;  
       array[k++]=i;  
       i=-1;  
       display(nm,rs,resource,b);  
     }  
    }  
  }  
 for(i=0;i<4;i++)  
 {  
   if(flag[i])  
     k=1;  
   else {  
     k=0;  
     break;  
     }  
 }  
 if(k==1)  
 {  
   printf("\n System is in safe state with sequence");  
 }  
 for(i=0;i<nm;i++)  printf(" %d",array[i]);  
 //getch();  
 return k;  
 }
