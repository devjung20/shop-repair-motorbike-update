import { DatePicker } from "antd";
import { styled } from "styled-components";
import { toDDMMYYYY } from "../utils/converter";
import dayjs from "dayjs";
import { ArrowRightOutlined } from "@ant-design/icons";

const StyledSpan = styled.span`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const DateRangeCardHeader = ({ title, value, onChange = () => {} }) => {
  const dateFormat = "DD/MM/YYYY";
  const range = [toDDMMYYYY(value?.startTime), toDDMMYYYY(value?.endTime)];

  return (
    <StyledSpan>
      <span>{title}</span>
      <DatePicker.RangePicker
        value={[dayjs(range[0], dateFormat), dayjs(range[1], dateFormat)]}
        placeholder={["Từ ngày", "Tới ngày"]}
        format={dateFormat}
        onChange={onChange}
        separator={<ArrowRightOutlined />}
        style={{ width: "225px" }}
      />
    </StyledSpan>
  );
};

export default DateRangeCardHeader;
