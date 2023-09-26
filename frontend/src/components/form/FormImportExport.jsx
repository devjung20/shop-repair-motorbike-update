import { Col, Form, Input, InputNumber, Row, Select, Table } from "antd";
import { useEffect, useState } from "react";
import { apiCaller } from "../../utils/api";
import productAPI from "../../api/requests/product";
import { intToString, stringToInt } from "../../utils/converter";

const FormImportExport = ({ form, onFinish, type = "EXPORT" }) => {
  const [selectOptions, setSelectOptions] = useState([]);
  const [lines, setLines] = useState([]);
  const [note, setNote] = useState("");

  useEffect(() => {
    apiCaller(
      productAPI.getAll({
        pageNumber: 1,
        pageSize: 1000,
      }),
      (data) => {
        let dataOpts = data?.data?.content;
        if (type === "EXPORT") {
          dataOpts = dataOpts.filter((opt) => opt.storageQuantity > 0);
        }

        dataOpts = dataOpts?.map((opt) => ({
          value: opt.id,
          label: opt.name,
          ...opt,
        }));
        setSelectOptions(dataOpts);
      }
    );
  }, [type]);

  const handleSelectChange = (values) => {
    // If select 0 option
    if (values.length === 0) {
      setLines([]);
      return;
    }

    const lastLineId = values[values.length - 1];

    // If add option
    if (values.length > lines.length) {
      const lineProduct = selectOptions.find((opt) => opt.value === lastLineId);

      const newLine = {
        id: lineProduct.value,
        name: lineProduct.label,
        unit: lineProduct.unit,
        storageQuantity: lineProduct.storageQuantity,
        difference: 1,
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
      title: "Tên linh kiện",
      dataIndex: "name",
      width: "50%",
    },
    {
      title: "Đơn vị",
      dataIndex: "unit",
      align: "center",
    },
    {
      title: "Tồn kho",
      dataIndex: "storageQuantity",
      align: "center",
    },
    {
      title: "Số lượng",
      dataIndex: "difference",
      align: "center",
      render: (difference, line) => (
        <InputNumber
          step={1}
          min={1}
          formatter={intToString}
          parser={stringToInt}
          max={type === "EXPORT" ? line.storageQuantity : null}
          value={difference}
          onChange={(value) => {
            const newLines = [...lines];
            const newLine = newLines.find((l) => l.id === line.id);
            newLine.difference = value;
            setLines(newLines);
          }}
          onStep={(value) => {
            const newLines = [...lines];
            const newLine = newLines.find((l) => l.id === line.id);
            newLine.difference = value;
            setLines(newLines);
          }}
        />
      ),
    },
  ];

  return (
    <Form
      form={form}
      layout="vertical"
      onFinish={() => {
        onFinish({
          note: note,
          historyDetails: lines.map((l) => ({ productId: l.id, difference: l.difference })),
        });
      }}
    >
      <Row gutter={12}>
        <Col span={24}>
          <Select
            showSearch
            mode="multiple"
            className="w-100 mb-6"
            placeholder="Chọn linh kiện"
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
        </Col>
        <Col span={24}>
          <Input.TextArea rows={4} placeholder="Nhập ghi chú" value={note} onChange={(e) => setNote(e.target.value)} />
        </Col>
      </Row>
    </Form>
  );
};

export default FormImportExport;