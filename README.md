# MdView

纯前端 Markdown 查看器与 PDF 导出工具，支持 LaTeX 数学公式。无需后端、无需登录。

## 功能

- 打开本地 `.md` 文件，或直接粘贴 Markdown 文本
- 实时渲染，支持 `$行内公式$` 与 `$$独立公式$$`（KaTeX）
- 一键导出 PDF（调用浏览器打印，选"另存为 PDF"）
- 手机、电脑浏览器均可使用

## 网页版

`index.html` 即完整应用，单文件自包含。推送到本仓库后：

1. 仓库 **Settings → Pages**，Source 选 `main` 分支根目录；
2. 稍等片刻，得到 `https://<你的用户名>.github.io/MdView/`；
3. 手机浏览器打开该网址即可使用。

## 安卓版

`android/` 为 WebView 工程，网页版已打包进 `assets/`，离线可用。

每次 push 到 `main`，GitHub Actions（`.github/workflows/android.yml`）自动编译 `app-debug.apk`，可在仓库 **Actions** 页下载。

> 注：debug 版 APK 可直接安装；如需上架应用市场，需另配签名 release 版。

## 技术栈

- 渲染：[marked](https://marked.js.org/) + [KaTeX](https://katex.org/)（jsDelivr CDN）
- PDF：浏览器原生打印
- 安卓：Android WebView + GitHub Actions 自动构建
