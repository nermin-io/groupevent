import type { NextApiRequest, NextApiResponse } from "next";
import Groupevent from "@/clients/groupevent";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  const response = await Groupevent.post("/organisers/login", req.body);
  return res
    .status(response.status)
    .json(response.data);
}
