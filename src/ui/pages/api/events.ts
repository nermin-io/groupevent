import type { NextApiRequest, NextApiResponse } from "next";
import Groupevent from "../../clients/groupevent";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  const { organiser, data } = req.body;
  const response = await Groupevent.post(`organisers/${organiser}/events`, data);
  return res
    .status(response.status)
    .json(response.data);
}
