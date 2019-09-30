# Shop-pin Backend

Shop Pin 拼团电商项目后端代码。本项目仅用于学习交流之目的。若欲将其部分用于商业用途，须得作者之许可。

本项目主要基于 Spring Boot 基本框架。并使用 Spring Cloud （Eureka\Feign） 搭建分布式架构，并结合了多种技术将原仅适用与单服务器的模块进行分布式架构之适配。同时附带一个自更新的简单的推荐系统，针对用户推荐首页商品。

## 主要使用的框架

### 后端业务逻辑

- Spring Boot

- Spring Cloud (Distributed)

- Spring Security (RBAC)

- Spring AOP

### 通信与存储

- Redis (Cache / Mutex Lock for Critical Condition)

- MongoDB (Rich text and image storage / Recommendation rank cache)

- RabbitMQ (Cross-nodes communication / Distributed Web-socket)

### 基础推荐服务策略

- Flask (Recommender Trigger Entry Point)

- Tensorflow

## 开源协定

本项目遵循 MIT 开源协定之规定。
