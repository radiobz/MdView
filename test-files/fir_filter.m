% MATLAB 测试脚本：FIR 滤波器设计
% 演示：使用 FDAtool 等价代码设计低通滤波器

Fs = 1000;          % 采样率 1000 Hz
Fc = 150;           % 截止频率 150 Hz
N  = 64;            % 滤波器阶数

% 设计 Hamming 窗 FIR 低通
b = fir1(N, Fc/(Fs/2), hamming(N+1));

% 生成测试信号：50Hz + 300Hz
t = 0:1/Fs:1;
x = sin(2*pi*50*t) + 0.5*sin(2*pi*300*t);

% 滤波
y = filtfilt(b, 1, x);

% 绘图
figure;
subplot(2,1,1); plot(t, x); title('原始信号（含高频噪声）');
subplot(2,1,2); plot(t, y); title('滤波后信号（保留50Hz）');

fprintf('滤波器设计完成，阶数=%d\n', N);
