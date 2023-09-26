import { Select } from "antd";
import { styled } from "styled-components";

const StyledSpan = styled.span`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const SelectCardHeader = ({
  title,
  value,
  options = [
    { label: "6 tháng gần nhất", value: 6 },
    { label: "12 tháng gần nhất", value: 12 },
    { label: "24 tháng gần nhất", value: 24 },
  ],
  onChange = () => {},
}) => {
  return (
    <StyledSpan>
      <span>{title}</span>
      <Select value={value} options={options} onChange={onChange} />
    </StyledSpan>
  );
};

export default SelectCardHeader;
