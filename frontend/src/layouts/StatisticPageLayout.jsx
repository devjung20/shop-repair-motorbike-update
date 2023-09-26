import { Button, DatePicker, Typography } from "antd";
import Container from "../components/styled/Container";
import { ArrowRightOutlined, CloudDownloadOutlined } from "@ant-design/icons";
import { styled } from "styled-components";
import dayjs from "dayjs";
import { toDDMMYYYY, toYYYY_MM_DD } from "../utils/converter";
import { useLocation, useNavigate } from "react-router-dom";
import { useState } from "react";
import queryString from "query-string";

const { Title } = Typography;

const ListHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
`;

const ListExportButton = styled(Button)`
  display: flex;
  align-items: center;

  svg {
    font-size: 20px;
  }
`;

const StatisticPageLayout = ({
  size = "lg",
  title = "Thống kê",
  exportText = "Xuất báo cáo",
  navigatePath = "",
  handleExport = () => {},
  children,
}) => {
  const dateFormat = "DD/MM/YYYY";
  const navigate = useNavigate();
  const location = useLocation();
  const { startTime = dayjs().subtract(1, "month"), endTime } = queryString.parse(location.search);

  const [range, setRange] = useState([toDDMMYYYY(startTime), toDDMMYYYY(endTime)]);

  return (
    <Container size={size}>
      <Title level={2}>{title}</Title>
      <ListHeader>
        <div>
          <div className="my-3">Khoảng thời gian</div>
          <DatePicker.RangePicker
            defaultValue={[dayjs(range[0], dateFormat), dayjs(range[1], dateFormat)]}
            placeholder={["Từ ngày", "Tới ngày"]}
            format={dateFormat}
            onChange={([start, end]) => {
              setRange([toDDMMYYYY(start), toDDMMYYYY(end)]);
              navigate(`${navigatePath}?startTime=${toYYYY_MM_DD(start)}&endTime=${toYYYY_MM_DD(end)}`);
            }}
            separator={<ArrowRightOutlined />}
            size="large"
          />
        </div>
        <ListExportButton type="primary" onClick={handleExport} size="large">
          <CloudDownloadOutlined /> {exportText}
        </ListExportButton>
      </ListHeader>
      {children}
    </Container>
  );
};

export default StatisticPageLayout;
