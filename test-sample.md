# 实验笔记：线性回归与最小二乘

> 本文件用于测试 MdView 的多级标题、数学公式、表格与图片渲染。

## 1. 问题定义

给定 $N$ 个观测样本 $(x_i, y_i)$，我们希望用一条直线

$$
\hat{y} = w x + b
$$

来拟合数据，并最小化误差平方和：

$$
J(w, b) = \sum_{i=1}^{N} \left( y_i - (w x_i + b) \right)^2
$$

其中行内变量如 $w$、$b$、$\sigma^2$ 均应正常渲染。

### 1.1 梯度下降法

参数沿负梯度方向更新：

$$
w \leftarrow w - \eta \frac{\partial J}{\partial w}, \qquad
b \leftarrow b - \eta \frac{\partial J}{\partial b}
$$

闭式解（矩阵形式）为：

$$
\hat{\boldsymbol{w}} = (\mathbf{X}^\top \mathbf{X})^{-1} \mathbf{X}^\top \boldsymbol{y}
$$

## 2. 实验数据

下表是一次模拟实验的部分结果：

| 学习率 $\eta$ | 迭代次数 | 训练误差 | 测试误差 |
| --- | --- | ---: | ---: |
| 0.001 | 1000 | 0.124 | 0.158 |
| 0.010 | 1000 | 0.031 | 0.047 |
| 0.100 | 1000 | 0.028 | 0.052 |
| 1.000 | 1000 | 不收敛 | 不收敛 |

可以看到 $\eta = 0.01$ 时效果最佳，过大的学习率会导致震荡。

## 3. 拟合曲线示例

下图是某组数据的拟合结果：

![线性回归拟合示例](https://picsum.photos/id/0/600/300)

残差近似服从正态分布：

$$
r_i \sim \mathcal{N}(0, \sigma^2), \qquad \sigma \approx 0.047
$$

## 4. 代码片段

```python
import numpy as np

X = np.array([...])   # 设计矩阵
y = np.array([...])
w = np.linalg.inv(X.T @ X) @ X.T @ y
print(w)
```

## 5. 小结

- 多级标题：本文件包含 `#`、`##`、`###` 三级标题；
- 公式：行内 $E=mc^2$ 与独立公式均已测试；
- 表格、图片、代码块齐全；
- 点击右上角「导出 PDF」即可另存。
