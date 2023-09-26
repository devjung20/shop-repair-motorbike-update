import { Col, Form, InputNumber, Row, Select, Table } from "antd";
import { useEffect, useMemo, useState } from "react";
import { apiCaller } from "../../utils/api";
import serviceAPI from "../../api/requests/service";
import { moneyRenderer } from "../../utils/renderer";
import { intToString, stringToInt } from "../../utils/converter";

const FormOrderService = ({
  form,
  onFinish,
  initialValues = { services: [] },
  setTotal = (value) => console.log(value),
}) => {
  const [selectOptions, setSelectOptions] = useState([]);
  const [selected, setSelected] = useState([]);
  const [lines, setLines] = useState([]);
  const totalPrice = useMemo(() => lines.reduce((sum, line) => (sum += line.price * line.quantity), 0), [lines]);

  useEffect(() => {
    setTotal(totalPrice);
  }, [totalPrice, setTotal]);

  useEffect(() => {
    if (initialValues?.services) {
      const serviceLines = initialValues.services.map((srv) => ({
        ...srv,
        totalPrice: srv.price * srv.quantity,
      }));
      const serviceSelected = serviceLines.map((line) => line.id);
      setLines(serviceLines);
      setSelected(serviceSelected);
    }
  }, [initialValues?.services]);

  useEffect(() => {
    apiCaller(
      serviceAPI.getAll({
        pageNumber: 1,
        pageSize: 1000,
      }),
      (data) => {
        const dataOpts = data?.data?.content?.map((opt) => ({
          value: opt.id,
          label: opt.name,
          price: opt.price,
        }));
        setSelectOptions(dataOpts);
      }
    );
  }, []);

  const handleSelectChange = (values) => {
    setSelected(values);

    // If select 0 option
    if (values.length === 0) {
      setLines([]);
      return;
    }

    const lastLineId = values[values.length - 1];

    // If add option
    if (values.length > lines.length) {
      const lineService = selectOptions.find((opt) => opt.value === lastLineId);
      const newLine = {
        id: lineService.value,
        name: lineService.label,
        price: lineService.price,
        quantity: 1,
        totalPrice: lineService.price,
      };
      setLines([...lines, newLine]);
    }

    // If remove option
    else if (values.length < lines.length) {
      const newLines = lines.filter((line) => values.includes(line.id));
      setLines(newLines);
    }
  };

  const columns = [
    {
      title: "Tên dịch vụ",
      dataIndex: "name",
      width: "50%",
    },
    {
      title: "Đơn giá",
      dataIndex: "price",
      align: "center",
      render: moneyRenderer,
    },
    {
      title: "Số lượng",
      dataIndex: "quantity",
      align: "center",
      render: (quantity, line) => (
        <InputNumber
          min={1}
          step={1}
          value={quantity}
          formatter={intToString}
          parser={stringToInt}
          onChange={(value) => {
            const newLines = [...lines];
            const newLine = newLines.find((l) => l.id === line.id);
            newLine.quantity = value;
            newLine.totalPrice = newLine.price * value;
            setLines(newLines);
          }}
          onStep={(value) => {
            const newLines = [...lines];
            const newLine = newLines.find((l) => l.id === line.id);
            newLine.quantity = value;
            newLine.totalPrice = newLine.price * value;
            setLines(newLines);
          }}
        />
      ),
    },
    {
      title: "Thành tiền",
      dataIndex: "totalPrice",
      align: "center",
      render: moneyRenderer,
    },
  ];

  return (
    <Form
      form={form}
      layout="vertical"
      onFinish={() => {
        onFinish({
          services: lines,
        });
      }}
    >
      <Row gutter={12}>
        <Col span={24}>
          <Select
            showSearch
            value={selected}
            mode="multiple"
            className="w-100 mb-6"
            placeholder="Chọn dịch vụ"
            onChange={handleSelectChange}
            filterOption={(input, option) => (option?.label ?? "").toLowerCase().includes(input.toLowerCase())}
            optionFilterProp="children"
            optionLabelProp="label"
            options={selectOptions}
          />
        </Col>
        <Col span={24} className="mb-6">
          <Table
            size="small"
            columns={columns}
            pagination={false}
            rowKey={(record) => record.id}
            dataSource={lines}
            scroll={{
              x: 576,
            }}
          />
          <div className="mt-6 text-right pr-6">
            <span className="text-semibold">Tổng cộng:</span> {moneyRenderer(totalPrice)}
          </div>
        </Col>
      </Row>
    </Form>
  );
};

export default FormOrderService;
