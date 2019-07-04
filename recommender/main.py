import numpy as np
import tensorrec

# Build the model with default parameters
from tensorrec.representation_graphs import ReLURepresentationGraph
from scipy.sparse import csr_matrix

model = tensorrec.TensorRec(item_repr_graph=ReLURepresentationGraph(), user_repr_graph=ReLURepresentationGraph())

# Generate some dummy data
interactions, user_features, item_features = tensorrec.util.generate_dummy_data(num_users=2, num_items=5, interaction_density=.5)

interactions = csr_matrix

# 交互属性值 （矩阵大小用户数*商品数）
interactions = np.array([[1, 0, 0.5], [0, None, 0]])
# 性别，余额数量级，积分数量级
user_features = np.array([[1, 3, 2], [0, 2, 1]])
# 分类ID，价格，销量，访问量，好评率，产品得分
item_features = np.array([[1, 1638, 152, 1500, 0.998], [2, 98, 152, 1500, 0.96], [2, 66, 0.95]])

print("interaction: \n", interactions)
print("user: \n" + str(user_features))
print("item: \n" +str(item_features))

# Fit the model for 5 epochs
model.fit(interactions, user_features, item_features, epochs=5, verbose=True)

# Predict scores and ranks for all users and all items
predictions = model.predict(user_features=user_features,
                            item_features=item_features)
predicted_ranks = model.predict_rank(user_features=user_features,
                                     item_features=item_features)

print("Predictions: \n" +str(predictions))
print("Predicted Rank: \n" +str(predicted_ranks))

# Calculate and print the recall at 10
r_at_k = tensorrec.eval.recall_at_k(predicted_ranks, interactions, k=10)
print(np.mean(r_at_k))