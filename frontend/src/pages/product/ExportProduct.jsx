import { useNavigate } from "react-router-dom";
import Container from "../../components/styled/Container";
import { Button, Card, Form, Typography } from "antd";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import FormImportExport from "../../components/form/FormImportExport";
import productAPI from "../../api/requests/product";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";

const { Title } = Typography;

const ExportProduct = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();

  const handleExportProduct = (values) => {
    const correctValues = {
      note: values.note,
      historyDetails: values.historyDetails.map((val) => ({ productId: val.productId, difference: -val.difference })),
    };

    apiCaller(productAPI.createHistory(correctValues), () => {
      AntdSwal.fire("Thành công", "Xuất kho thành công!", "success");
      navigate("/stock-history");
    });
  };

  return (
    <Container size="lg">
      <Title level={2} className="mt-4">
        Xuất kho
      </Title>
      <Card title="Thông tin hàng xuất" bordered={false} className="mt-8">
        <FormImportExport form={form} onFinish={handleExportProduct} />
      </Card>
      <FormButtonGroup>
        <Button onClick={() => navigate("/employees")}>Hủy</Button>
        <Button type="primary" onClick={() => form.submit()}>
          Xuất kho
        </Button>
      </FormButtonGroup>
    </Container>
  );
};

export default ExportProduct;
