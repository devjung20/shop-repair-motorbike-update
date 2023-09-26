import { Link, useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Col, Form, Row, Typography } from "antd";
import FormProduct from "../../components/form/FormProduct";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import productAPI from "../../api/requests/product";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import FormImage from "../../components/form/FormImage";

const { Title } = Typography;

const EditProduct = () => {
  const navigate = useNavigate();
  const { productId } = useParams();
  const [formProduct] = Form.useForm();
  const [product, setProduct] = useState(null);
  const [imageUrl, setImageUrl] = useState(null);

  useEffect(() => {
    apiCaller(productAPI.getById(productId), (data) => {
      const productData = data.data;
      setProduct(productData);
      setImageUrl(productData.imageUrl);
    });
  }, [productId]);

  const handleEditProduct = (values) => {
    if (imageUrl) {
      const imageTokens = imageUrl.split("/");
      const imageId = imageTokens[imageTokens.length - 1];
      values.imageId = Number(imageId);
    }
    apiCaller(productAPI.updateById(productId, values), () => {
      AntdSwal.fire("Thành công", "Cập nhật linh kiện thành công", "success");
      navigate("/products");
    });
  };

  return (
    <Container size="lg">
      <Link to="/products">
        <LeftOutlined /> Quay lại danh sách linh kiện
      </Link>
      <Title level={2} className="mt-4">
        Cập nhật linh kiện {product?.code}
      </Title>
      <Row gutter={12}>
        <Col xs={24} sm={24} md={16}>
          <Card title="Thông tin linh kiện" bordered={false} className="mt-8">
            <FormProduct form={formProduct} onFinish={handleEditProduct} initialValues={product} />
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

export default EditProduct;
