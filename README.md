#  drools

# 场景1：

假定如下情景：网站伴随业务产生而进行的积分发放操作。比如支付宝信用卡还款奖励积分等。 <br>
我们定义一下发放规则： <br>
积分的发放参考因素有：交易笔数、交易金额数目、信用卡还款次数、生日特别优惠等。 <br>
定义规则： <br>
// 过生日，则加10分，并且将当月交易比数翻倍后再计算积分 <br>
// 2011-01-08 – 2011-08-08每月信用卡还款3次以上，每满3笔赠送30分<br>
// 当月购物总金额100以上，每100元赠送10分 <br>
// 当月购物次数5次以上，每五次赠送50分 <br>
// 特别的，如果全部满足了要求，则额外奖励100分<br>
// 发生退货，扣减10分 <br>
// 退货金额大于100，扣减100分<br>

# 要修改的文件

1、bootstrap.properties 文件中的eureka.client.serviceUrl.defaultZone参数修改<br>
2、application-dev.properties中有关数据库连接的参数需要修改

3、src/main/doc/ratingTest2.drl 需要放到本地D:/work/drlPackage/文件夹下；<br>
可以在application-dev.properties中修改路径


# 场景2：
{
    "lists": [
        {
            "keypointId": "3",
            "keypointParentId": "1",
            "keypointSortId": "1",
            "weight": "0.30",
            "score": "4.00",
            "isLeaf": true
        },
        {
            "keypointId": "4",
            "keypointParentId": "1",
            "keypointSortId": "2",
            "weight": "0.30",
            "score": "3.00",
            "isLeaf": true
        },
        {
            "keypointId": "5",
            "keypointParentId": "1",
            "keypointSortId": "3",
            "weight": "0.40",
            "score": "3.00",
            "isLeaf": true
        },
        {
            "keypointId": "6",
            "keypointParentId": "2",
            "keypointSortId": "1",
            "weight": "0.20",
            "score": "3.00",
            "isLeaf": true
        },
        {
            "keypointId": "7",
            "keypointParentId": "2",
            "keypointSortId": "2",
            "weight": "0.80",
            "score": "4.00",
            "isLeaf": true
        },
        {
            "keypointId": "1",
            "keypointParentId": "0",
            "keypointSortId": "1",
            "weight": "0.30",
            "score": null,
            "isLeaf": false
        },
        {
            "keypointId": "2",
            "keypointParentId": "0",
            "keypointSortId": "2",
            "weight": "0.70",
            "score": null,
            "isLeaf": false
        }
    ]
}

计算：
keypointId=1: score=0.3*4+0.3*3+0.4*3 = 3.3
keypointId=2: score=0.2*3+0.8*4 = 3.8

总得分为： 0.3*3.3 + 0.7*3.8 = 3.65
