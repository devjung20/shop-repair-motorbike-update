import { Link, useNavigate } from "react-router-dom";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Form, Typography } from "antd";
import { toYYYY_MM_DD } from "../../utils/converter";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import FormService from "../../components/form/FormService";
import serviceAPI from "../../api/requests/service";

const { Title } = Typography;

const AddService = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();

  const handleAddService = (values) => {
    values.birthday = toYYYY_MM_DD(values.birthday);
    apiCaller(serviceAPI.create(values), () => {
      AntdSwal.fire("Thành công", "Thêm dịch vụ thành công", "success");
      navigate("/services");
    });
  };

  return (
    <Container size="lg">
      <Link to="/employees">
        <LeftOutlined /> Quay lại danh sách dịch vụ
      </Link>
      <Title level={2} className="mt-4">
        Thêm mới dịch vụ
      </Title>
      <Card title="Thông tin nhân viên" bordered={false} className="mt-8">
        <FormService form={form} onFinish={handleAddService} />
      </Card>
      <FormButtonGroup>
        <Button onClick={() => navigate("/services")}>Hủy</Button>
        <Button type="primary" onClick={() => form.submit()}>
          Lưu
        </Button>
      </FormButtonGroup>
    </Container>
  );
};

export default AddService;
