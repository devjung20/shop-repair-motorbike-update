import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import Container from "../../components/styled/Container";
import { Button, Card, Form, Typography } from "antd";
import AntdSwal from "../../custom/AntdSwal";
import { apiCaller } from "../../utils/api";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import FormStore from "../../components/form/FormStore";
import storeAPI from "../../api/requests/store";

const { Title } = Typography;

const SettingStore = () => {
  const [store, setStore] = useState(null);
  const navigate = useNavigate();
  const [form] = Form.useForm();

  useEffect(() => {
    apiCaller(storeAPI.getStore(), (data) => setStore(data.data));
  }, []);

  const handleSettingStore = (values) => {
    apiCaller(storeAPI.updateStore(values), () => {
      AntdSwal.fire("Thành công", "Cập nhật cửa hàng thành công", "success");
      navigate("/");
    });
  };

  return (
    <Container size="lg">
      <Title level={2}>Cài đặt cửa hàng</Title>
      <Card title="Thông tin cửa hàng" bordered={false} className="mt-8">
        <FormStore form={form} onFinish={handleSettingStore} initialValues={store} />
      </Card>
      <FormButtonGroup>
        <Button type="primary" onClick={() => form.submit()}>
          Lưu cài đặt
        </Button>
      </FormButtonGroup>
    </Container>
  );
};

export default SettingStore;
