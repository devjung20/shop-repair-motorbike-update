import { Card, Col, Row, Space, Tag, Typography } from "antd";
import ExportButton from "../../components/styled/ExportButton";
import Container from "../../components/styled/Container";
import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import orderAPI from "../../api/requests/order";
import { apiCaller } from "../../utils/api";
import CardData from "../../components/CardData";
import { employeeStatusRenderer, moneyRenderer } from "../../utils/renderer";
import TableInvoiceProduct from "../../components/table/TableInvoiceProduct";
import TableInvoiceService from "../../components/table/TableInvoiceService";
import { LeftOutlined } from "@ant-design/icons";

const DetailInvoice = () => {
  const invoiceId = useParams().invoiceId;
  const [invoice, setInvoice] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!invoiceId) return;

    setLoading(true);
    apiCaller(orderAPI.getById(invoiceId), (data) => {
      setInvoice(data.data);
      setLoading(false);
    });
  }, [invoiceId]);

  const totalProductPrice = invoice?.infoProducts?.reduce((sum, data) => (sum += data.price * data.quantity), 0);
  const totalServicePrice = invoice?.infoServices?.reduce((sum, data) => (sum += data.price * data.quantity), 0);

  return (
    <Container size="lg">
      <Link to="/invoices">
        <LeftOutlined /> Quay lại danh sách hóa đơn
      </Link>
      <Typography.Title level={2} className="mt-4">
        Hóa đơn {invoice?.code}
      </Typography.Title>
      <ExportButton
        type="link"
        onClick={() => {
          const rootPath = window.location.origin;
          const relativePath = "/invoice/invoice.html?invoiceId=" + invoiceId;
          window.open(rootPath + relativePath, "_blank");
        }}
      >
        Xuất hóa đơn
      </ExportButton>
      <Row gutter={16} className="mt-8">
        <Col xs={24} md={12} lg={8}>
          <Card title="Thông tin khách hàng" bordered={false} className="mb-4">
            <CardData
              data={invoice?.infoCustomer}
              labels={[
                { title: "Mã KH", dataIndex: "code" },
                { title: "Tên KH", dataIndex: "name" },
                { title: "Điện thoại", dataIndex: "phone" },
                { title: "Loại xe", dataIndex: "motorbikeName" },
                { title: "Biển số", dataIndex: "motorbikeCode" },
              ]}
            />
          </Card>
        </Col>
        <Col xs={24} md={12} lg={8}>
          <Card title="Nhân viên điều phối" bordered={false} className="mb-4">
            <CardData
              data={invoice?.infoDispatcher}
              labels={[
                { title: "Mã NV", dataIndex: "code" },
                { title: "Tên NV", dataIndex: "name" },
                { title: "Điện thoại", dataIndex: "phone" },
                { title: "Email", dataIndex: "email" },
                {
                  title: "Trạng thái",
                  dataIndex: "status",
                  render: (status) => (
                    <Tag color={status ? "#66B8FF" : "error"} key={Boolean(status)} style={{ borderRadius: 50 }}>
                      {employeeStatusRenderer(status)}
                    </Tag>
                  ),
                },
              ]}
            />
          </Card>
        </Col>
        <Col xs={24} md={12} lg={8}>
          <Card title="Nhân viên sửa chữa" bordered={false} className="mb-4">
            <CardData
              data={invoice?.infoRepairer}
              labels={[
                { title: "Mã NV", dataIndex: "code" },
                { title: "Tên NV", dataIndex: "name" },
                { title: "Điện thoại", dataIndex: "phone" },
                { title: "Email", dataIndex: "email" },
                {
                  title: "Trạng thái",
                  dataIndex: "status",
                  render: (status) => (
                    <Tag color={status ? "#66B8FF" : "error"} key={Boolean(status)} style={{ borderRadius: 50 }}>
                      {employeeStatusRenderer(status)}
                    </Tag>
                  ),
                },
              ]}
            />
          </Card>
        </Col>
        <Col xs={24} md={12} lg={24}>
          <Card title="Ghi chú" bordered={false} className="mb-4" style={{ height: "calc(100% - 1rem)" }}>
            {invoice?.note}
          </Card>
        </Col>
        <Col span={24}>
          <Card title="Dịch vụ sử dụng" bordered={false} className="mb-4">
            <TableInvoiceService
              dataSource={invoice?.infoServices}
              loading={loading}
              totalServicePrice={totalServicePrice}
            />
          </Card>
        </Col>
        <Col span={24}>
          <Card title="Linh kiện sử dụng" bordered={false} className="mb-4">
            <TableInvoiceProduct
              dataSource={invoice?.infoProducts}
              loading={loading}
              totalProductPrice={totalProductPrice}
            />
          </Card>
        </Col>
        <Col span={24}>
          <Card bordered={false} className="mb-4">
            <Typography.Title level={4} className="mb-0">
              <Space className="mr-4" style={{ display: "flex", justifyContent: "center" }}>
                <span>Tổng thu cuối:</span>
                <span>{moneyRenderer(totalServicePrice + totalProductPrice)}</span>
              </Space>
            </Typography.Title>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default DetailInvoice;
