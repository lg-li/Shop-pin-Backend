import tensorrec
from flask import Flask, request, jsonify
from scipy.sparse import csr_matrix
from tensorrec.representation_graphs import ReLURepresentationGraph

app = Flask(__name__)

model = tensorrec.TensorRec(item_repr_graph=ReLURepresentationGraph(), user_repr_graph=ReLURepresentationGraph())
try:
    model = model.load_model('./model')
except IOError:
    print("没有保存的模型检查点，初始化模型")

@app.route('/ranks', methods=["POST"])
def fitModelAndUpateRanks():
    input = request.get_json()
    print("INPUT: \n" + str(input) + "\n")
    interactions = input['interaction']
    user_features = input['user']
    item_features = input['product']
    # 交互属性值 （矩阵大小用户数*商品数）
    interactions = csr_matrix((interactions['data'], (interactions['row'], interactions['col'])))
    # 性别，余额数量级，积分数量级
    user_features = csr_matrix((user_features['data'], (user_features['row'], user_features['col'])))
    # 分类ID，价格，销量，访问量，好评率，产品得分
    item_features = csr_matrix((item_features['data'], (item_features['row'], item_features['col'])))

    print("interaction: \n", interactions)
    print("user: \n" + str(user_features))
    print("item: \n" +str(item_features))

    # Fit the model for 5 epochs
    model.fit(interactions, user_features, item_features, epochs=5, verbose=True)
    model.save_model("./model")
    # Predict scores and ranks for all users and all items
    predictions = model.predict(user_features=user_features,
                                item_features=item_features)
    predicted_ranks = model.predict_rank(user_features=user_features,
                                         item_features=item_features)
    print("Predictions: \n" +str(predictions))
    print("Predicted Rank: \n" +str(predicted_ranks))
    return jsonify(predicted_ranks.tolist())

port = 5689
if __name__ == "__main__":
    print("启动 Pin 推荐服务...")
    print("将启动到端口" + str(port))
    app.run(host='0.0.0.0', port=port)