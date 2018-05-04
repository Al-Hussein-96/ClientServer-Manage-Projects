#include <bits/stdc++.h>
#define ll long long
#define F first
#define S second
#define mp make_pair
#define All(v) v.begin(),v.end()
#define mod 1000000007
using namespace std;
int a[100005];
int n,k;

int main()
{
    cin >> n >> k;
    for(int i=0; i<n; i++)
        cin >> a[i];

    int res = -2e9;

    int j = 1;
    multiset<int> my;
    for(int i=1;i<n-k;i++)
    {
        my.insert(a[i]-a[i-1]);
    }
    res = *my.rbegin();

    for(int i=n-k;i<n;i++)
    {
        my.insert(a[i]-a[i-1]);
        my.erase(my.find(a[j]-a[j-1]));
        j++;
        res = min(res,*my.rbegin());
    }

    cout << res << endl;

    return 0;
}
