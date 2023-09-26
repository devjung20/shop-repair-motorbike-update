import { Link, useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Form, Typography } from "antd";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import serviceAPI from "../../api/requests/service";
import FormService from "../../components/form/FormService";

const { Title } = Typography;

const EditService = () => {
  const { serviceId } = useParams();
  const [service, setService] = useState(null);
  const navigate = useNavigate();
  const [form] = Form.useForm();

  useEffect(() => {
    apiCaller(serviceAPI.getById(serviceId), (data) => {
      const service = data.data;
      setService(service);
    });
  }, [serviceId]);

  const handleEditService = (values) => {
    apiCaller(serviceAPI.updateById(serviceId, values), () => {
      AntdSwal.fire("Thành công", "Cập nhật dịch vụ thành công", "success");
      navigate("/services");
    });
  };

  return (
    <Container size="lg">
      <Link to="/services">
        <LeftOutlined /> Quay lại danh sách dịch vụ
      </Link>
      <Title level={2} className="mt-4">
        Cập nhật dịch vụ {service?.code}
      </Title>
      <Card title="Thông tin cơ bản" bordered={false} className="mt-8">
        <FormService form={form} onFinish={handleEditService} initialValues={service} />
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

export default EditService;
