import { Col, Row } from "antd";
import { styled } from "styled-components";

const StyledTitle = styled.span`
  font-weight: 600;
  display: inline-block;
  width: 120px;
  margin-bottom: 0.5rem;
`;

const TableRowExpand = ({ record, labels }) => {
  return (
    <Row style={{ paddingLeft: 50 }}>
      {labels.map((label) => {
        let value = record[label.dataIndex];

        if (label.render) {
          value = label.render(value);
        }

        return (
          <Col xs={24} lg={12} key={label.dataIndex}>
            <StyledTitle>{label.title}</StyledTitle>
            <span>{value}</span>
          </Col>
        );
      })}
    </Row>
  );
};

export default TableRowExpand;
