# RecyclerView
###            ![](/screen/RecyclerView.gif) <br>

1、在此基础上面修改(https://github.com/Jude95/EasyRecyclerView).  
2、添加了AutoLayout, 已经适配到AutoLayout(https://github.com/hongyangAndroid/AndroidAutoLayout).  
3、使用方法(https://github.com/Jude95/EasyRecyclerView).  
4、修改BaseViewHolder.  
5、修改RecyclerArrayAdapter.java 的resId.  
6、修改EasyRecyclerView的showEmpty，让RecyclerView和Empty同时显示.  
7、修改DefaultEvenDelegate.java showNoMore() 隐藏noMore Footer.  
8、增加DefaultAdapter  设置默认选项.  
9、修改showError 空的时候Error和不空的时候Error.  
10、修改数据为空的时候，自定义设置点击刷新. 
11、增加的item动画. <br>
12、设置adapter
    
    adapter = new RecyclerArrayAdapter<Person>(this, R.layout.item_person){
            @Override
            protected void convert(BaseViewHolder viewHolder, Person item) {
                viewHolder.setText(R.id.person_name, item.getName());
                viewHolder.setText(R.id.person_sign, item.getSign());
                viewHolder.setImageUrl(R.id.person_face, item.getFace(), R.mipmap.ic_launcher);
            }
        };
    
13、设置头部
    
    adapter.addHeader(new DefaultRecyclerViewItem(){
            @Override
            public View onCreateView(ViewGroup parent) {
                View view =  LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.home_head_one, parent, false);
                return view;
            }
        });
14、设置点击事件
    
    adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                adapter.remove(position);
                return true;
            }
        });
        
15、 使用

```java
    Add it in your root build.gradle at the end of repositories: 
        maven { url "https://jitpack.io" } 
    Add the dependency  
        compile 'com.github.gzfgeh:GRecyclerView:v1.0.1' 
```
    
