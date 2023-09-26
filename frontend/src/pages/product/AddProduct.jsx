import { Link, useNavigate } from "react-router-dom";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Col, Form, Row, Typography } from "antd";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import productAPI from "../../api/requests/product";
import FormProduct from "../../components/form/FormProduct";
import FormImage from "../../components/form/FormImage";
import { useState } from "react";

const { Title } = Typography;

const AddProduct = () => {
  const navigate = useNavigate();
  const [formProduct] = Form.useForm();
  const [imageUrl, setImageUrl] = useState(null);
  const [product] = useState({
    name: "",
    brand: "",
    priceIn: null,
    priceOut: null,
    unit: "",
    storageQuantity: null,
    description: "",
  });

  const handleAddProduct = (values) => {
    if (imageUrl) {
      const imageTokens = imageUrl.split("/");
      const imageId = imageTokens[imageTokens.length - 1];
      values.imageId = Number(imageId);
    }
    apiCaller(productAPI.create(values), () => {
      AntdSwal.fire("Thành công", "Thêm linh kiện thành công", "success");
      navigate("/products");
    });
  };

  return (
    <Container size="lg">
      <Link to="/products">
        <LeftOutlined /> Quay lại danh sách linh kiện
      </Link>
      <Title level={2} className="mt-4">
        Thêm mới linh kiện
      </Title>
      <Row gutter={12}>
        <Col xs={24} sm={24} md={16}>
          <Card title="Thông tin linh kiện" bordered={false} className="mt-8">
            <FormProduct form={formProduct} onFinish={handleAddProduct} initialValues={product} />
          </Card>
        </Col>
        <Col xs={24} sm={24} md={8}>
          <Card title="Hình ảnh" bordered={false} className="mt-8">
            <FormImage imageUrl={imageUrl} setImageUrl={setImageUrl} />
          </Card>
        </Col>
      </Row>
      <FormButtonGroup>
        <Button onClick={() => navigate("/products")}>Hủy</Button>
        <Button type="primary" onClick={() => formProduct.submit()}>
          Lưu
        </Button>
      </FormButtonGroup>
    </Container>
  );
};

export default AddProduct;
