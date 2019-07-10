智能药箱
梁惠新，李永祺，叶蕴盈，邵明山，董亭    
   这是一个app,树莓派与数据库三方交互的项目,获得了第十四届齐鲁大学生软件大赛一等奖以及第四届全国大学生数字媒体科技作品竞赛三等奖。
  

# 智能药箱
梁惠新，李永祺，叶蕴盈，邵明山，董亭 
## 1.简介
下面是我实验中的一些环境依赖，版本只提供参考。

|环境/库|版本|
|:---------:|----------|
|Ubuntu|14.04.5 LTS|
|python|2.7.12|
|jupyter notebook|4.2.3|
|tensorflow-gpu|1.2.1|
|numpy|1.12.1|
|pandas|0.19.2|
|matplotlib|2.0.0|
|word2vec|0.9.1|
|tqdm|4.11.2|

## 2.文件结构

|- zhihu-text-classification<br/>
|　　|- raw_data　　　　　　　　　# 比赛提供的原始数据<br/>
|　　|- data　　　　　　　　　　　# 预处理得到的数据<br/>
|　　|- data_process　　　　　　　# 数据预处理代码<br/>
|　　|- models　　　　　　　　　　# 模型代码<br/>
|　　|　　|- wd-1-1-cnn-concat　　　　<br/>
|　　|　　|　　|- network.py　　　　　　# 定义网络结构<br/>
|　　|　　|　　|- train.py　　　　　　  # 模型训练<br/>
|　　|　　|　　|- predict.py　　　　　　# 验证集/测试集预测，生成概率矩阵<br/>
...<br/>
|　　|- ckpt　　　　　　　　　　　# 保存训练好的模型<br/>
|　　|- summary　　　　　　　　　　# tensorboard数据<br/>
|　　|- scores　　　　　　　　　　　# 测试集的预测概率矩阵<br/>
|　　|- local_scores　　　　　　　 # 验证集的预测概率矩阵<br/>
|　　|- doc　　　　　　　　　　    # 文档说明与相关论文<br/>
|　　|- notebook-old　　　　　　　# 比赛中未经过整理的代码<br/>
|　　|- local_ensemble.ipynb　　　# 验证集模型融合<br/>
|　　|- ensemble.py　　　　　　　　# 测试集模型融合<br/>
|　　|- data_helpers.py　　　　　　# 数据处理函数<br/>
|　　|- evaluator.py　　　　　　　 # 评价函数<br/>


