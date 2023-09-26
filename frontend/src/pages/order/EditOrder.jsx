import { Link, useNavigate, useParams } from "react-router-dom";
import Container from "../../components/styled/Container";
import { LeftOutlined } from "@ant-design/icons";
import { Button, Card, Form, Space, Typography } from "antd";
import FormButtonGroup from "../../components/styled/FormButtonGroup";
import FormOrderInfo from "../../components/form/FormOrderInfo";
import FormOrderService from "../../components/form/FormOrderService";
import FormOrderProduct from "../../components/form/FormOrderProduct";
import AntdSwal from "../../custom/AntdSwal";
import { useEffect, useState } from "react";
import { apiCaller } from "../../utils/api";
import orderAPI from "../../api/requests/order";
import { moneyRenderer } from "../../utils/renderer";

const { Title } = Typography;

const EditOrder = () => {
  const navigate = useNavigate();
  const orderId = useParams()?.orderId;
  const [formInfo] = Form.useForm();
  const [formService] = Form.useForm();
  const [formProduct] = Form.useForm();
  const [infoData, setInfoData] = useState(null);
  const [serviceData, setServiceData] = useState(null);
  const [productData, setProductData] = useState(null);
  const [flag1, setFlag1] = useState(false);
  const [flag2, setFlag2] = useState(false);
  const [flag3, setFlag3] = useState(false);
  const [totalPrice1, setTotalPrice1] = useState(0);
  const [totalPrice2, setTotalPrice2] = useState(0);

  useEffect(() => {
    apiCaller(orderAPI.getById(orderId), (data) => {
      const pureOrder = data?.data;
      setInfoData({
        customerId: pureOrder?.infoCustomer?.id,
        dispatcherId: pureOrder?.infoDispatcher?.id,
        repairerId: pureOrder?.infoRepairer?.id,
        motorbikeName: pureOrder?.infoCustomer?.motorbikeName,
        motorbikeCode: pureOrder?.infoCustomer?.motorbikeCode,
        note: pureOrder?.note,
      });
      setProductData({
        products: pureOrder?.infoProducts,
      });
      setServiceData({
        services: pureOrder?.infoServices,
      });
    });
  }, [orderId]);

  const handleEditInfo = (values) => {
    setInfoData(values);
    setFlag1(true);
  };

  const handleEditService = (values) => {
    if (values?.services?.length === 0) {
      AntdSwal.fire("Thao tác không hợp lệ", "Bạn phải thêm ít nhất một dịch vụ vào phiếu sửa chữa", "error");
      setFlag2(false);
    } else {
      setServiceData(values);
      setFlag2(true);
    }
  };

  const handleEditProduct = (values) => {
    setProductData(values);
    setFlag3(true);
  };

  const handleExportInvoice = () => {
    apiCaller(orderAPI.toInvoice(orderId), () => {
      AntdSwal.fire("Thành công", "Tạo hóa đơn thành công", "success");
      navigate("/invoices");
    });
  };

  useEffect(() => {
    if (flag1 && flag2 && flag3) {
      const submitData = { ...infoData, ...productData, ...serviceData, type: "ORDER" };
      apiCaller(orderAPI.updateById(orderId, submitData), () => {
        AntdSwal.fire("Thành công", "Cập nhật phiếu sửa chữa thành công", "success");
        navigate("/orders");
      });
    }
  }, [flag1, flag2, flag3, infoData, productData, serviceData, navigate, orderId]);

  return (
    <Container size="lg">
      <Link to="/orders">
        <LeftOutlined /> Quay lại danh sách phiếu sửa chữa
      </Link>
      <Title level={2} className="mt-4">
        Cập nhật phiếu sửa chữa {}
      </Title>
      <Card title="Thông tin phiếu sửa chữa" bordered={false} className="mt-8">
        <FormOrderInfo form={formInfo} onFinish={handleEditInfo} initialValues={infoData} />
      </Card>
      <Card title="Dịch vụ sử dụng" bordered={false} className="mt-8">
        <FormOrderService
          form={formService}
          onFinish={handleEditService}
          initialValues={serviceData}
          setTotal={setTotalPrice1}
        />
      </Card>
      <Card title="Linh kiện sử dụng" bordered={false} className="mt-8">
        <FormOrderProduct
          form={formProduct}
          onFinish={handleEditProduct}
          initialValues={productData}
          setTotal={setTotalPrice2}
        />
      </Card>
      <Card bordered={false} className="mb-4 mt-8">
        <Typography.Title level={4} className="mb-0">
          <Space className="mr-4" style={{ display: "flex", justifyContent: "center" }}>
            <span>Tổng tạm tính:</span>
            <span>{moneyRenderer(totalPrice1 + totalPrice2)}</span>
          </Space>
        </Typography.Title>
      </Card>
      <FormButtonGroup>
        <Button onClick={() => navigate("/orders")}>Hủy</Button>
        <Button
          type="primary"
          ghost
          onClick={() => {
            setFlag1(false);
            setFlag2(false);
            setFlag3(false);
            formInfo.submit();
            formService.submit();
            formProduct.submit();
          }}
        >
          Lưu
        </Button>
        <Button type="primary" onClick={handleExportInvoice}>
          Tạo hóa đơn
        </Button>
      </FormButtonGroup>
    </Container>
  );
};

export default EditOrder;
