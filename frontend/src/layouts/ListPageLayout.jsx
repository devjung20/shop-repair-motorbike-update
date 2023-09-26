import { Button, Card, Typography } from "antd";
import Container from "../components/styled/Container";
import { CloudDownloadOutlined, PlusCircleOutlined } from "@ant-design/icons";
import { styled } from "styled-components";

const { Title } = Typography;

const ListHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;

  & * {
    align-self: center;
  }
`;

const ListExportButton = styled(Button)`
  display: flex;
  align-items: center;
  padding-left: 0;
  padding-right: 0;

  svg {
    font-size: 23px;
  }
`;

const ListPageLayout = ({
  size = "lg",
  title = "",
  addable = true,
  cardTitle = "",
  addText = "",
  exportText = "",
  handleAdd = () => {},
  handleExport = () => {},
  children,
}) => {
  return (
    <Container size={size}>
      <ListHeader>
        <Title level={2}>{title}</Title>
        {addable && (
          <Button type="primary" size="large" onClick={handleAdd}>
            <PlusCircleOutlined /> {addText}
          </Button>
        )}
      </ListHeader>
      <ListExportButton type="link" onClick={handleExport}>
        <CloudDownloadOutlined /> {exportText}
      </ListExportButton>
      <Card title={cardTitle} bordered={false} className="mt-8">
        {children}
      </Card>
    </Container>
  );
};

export default ListPageLayout;
