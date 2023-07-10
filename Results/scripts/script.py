import re
import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

"""
Contains functions to plot charts from docker stats command 
comand is as follows 
    `while true do docker stats conatiner_name --no-stream | cat >> filename.csv`
"""

font = {"family": "Proxima Nova", "size": 12}
plt.rc("font", **font)

RS_COLOR = [219 / 255, 50 / 255, 143 / 255]
GRPC_COLOR = [100 / 255, 189 / 255, 189 / 255]
TEST_NAMES = [
    "request_response",
    "request_stream",
    "stream_response",
    "channel",
    "db_request_response",
    "db_request_stream",
    "db_bi_stream",
    "med_sized",
    "large_sized",
]
THREAD_STATS = ["10 Threads", "20 Threads", "50 Threads", "100 Threads"]
SIZED_THREAD_STATS = [
    "10 Threads\nSingle",
    "20 Threads\nSingle",
    "10 Threads\nStream",
    "20 Threads\nStream",
]


def get_only_characters(string):
    return re.sub(r"[^a-zA-Z]+", "", string)


def get_only_numbers(string):
    return float(re.sub(r"[^\d\.]", "", string))


def to_bit(value):
    return int(
        {
            "b": get_only_numbers(value) * 1,
            "kib": get_only_numbers(value) * 10e3,
            "kb": get_only_numbers(value) * 10e3,
            "mib": get_only_numbers(value) * 10e6,
            "mb": get_only_numbers(value) * 10e6,
            "gib": get_only_numbers(value) * 10e9,
            "gb": get_only_numbers(value) * 10e9,
        }.get(get_only_characters(value).lower(), 0)
    )


def split_on_slash(df_col, split_index):
    return df_col.apply(lambda x: x.split(" / ")[split_index])


def percentage_to_float(df_col):
    return df_col.apply(lambda x: float(x[0:-1]))


def make_throughput_chart(name: str):
    ''' Makes a throughput chart and saves it to a file with "name_throughput.png"'''
    rs_df = pd.read_csv("../container-csv/rs_" + name + "_tpt.csv")
    grpc_df = pd.read_csv("../container-csv/grpc_" + name + "_tpt.csv")
    rsocket_throughput_values = rs_df["Score"]
    grpc_throughput_values = grpc_df["Score"]

    width = 0.25
    fig, ax = plt.subplots(1, 1, figsize=(10, 7))

    plt.barh(
        [i - width / 2 for i in range(len(rsocket_throughput_values))],
        rsocket_throughput_values,
        color=RS_COLOR,
        label="RSocket",
        height=width,
    )
    plt.barh(
        [i + width / 2 for i in range(len(grpc_throughput_values))],
        grpc_throughput_values,
        color=GRPC_COLOR,
        label="gRPC",
        height=width,
    )

    thread_stats = THREAD_STATS
    if "sized" in name:
        thread_stats = SIZED_THREAD_STATS

    plt.yticks([i for i in range(len(rsocket_throughput_values))], thread_stats)
    plt.ylabel("Num of Threads")
    plt.xlabel("Throughput")
    plt.legend(bbox_to_anchor=(1.01, 1), loc="upper left")
    plt.title(f"Throughput of {name}")
    plt.grid()
    plt.tight_layout()
    plt.savefig("./graphs/" + name + "_throughput.png")
    plt.cla()
    plt.clf()


def make_mem_chart(name: str):
    ''' Makes a memory utilisation chart and saves it to a file with "name_throughput.png"'''

    rs_name = "rs_" + name
    grpc_name = "grpc_" + name

    rs_df = pd.read_csv(
        f"../container-csv/{rs_name}.csv", delimiter=r"\s\s+", engine="python"
    )
    rs_df = rs_df[rs_df.NAME != "NAME"]
    rs_df["MEM %"] = percentage_to_float(rs_df["MEM %"])

    grpc_df = pd.read_csv(
        f"../container-csv/{grpc_name}.csv", delimiter=r"\s\s+", engine="python"
    )
    grpc_df = grpc_df[grpc_df.NAME != "NAME"]
    grpc_df["MEM %"] = percentage_to_float(grpc_df["MEM %"])

    fig, ax = plt.subplots(1, 1, figsize=(14, 7))

    sns.lineplot(
        x=rs_df.index,
        y="MEM %",
        color=RS_COLOR,
        data=rs_df,
        drawstyle="steps",
        label="RSOCKET",
        linewidth=3,
    )
    sns.lineplot(
        x=grpc_df.index,
        y="MEM %",
        color=GRPC_COLOR,
        data=grpc_df,
        drawstyle="steps",
        label="GRPC",
        linewidth=2,
    )

    thread_stats = THREAD_STATS

    if "sized" in name:
        thread_stats = SIZED_THREAD_STATS

    x_min, x_max = np.min(grpc_df.index), np.max(grpc_df.index)
    intervals = np.linspace(x_min, x_max, 5)
    tick_pos = [(s + e) / 2 for (s, e) in zip(intervals[0:-1], intervals[1:])]

    plt.legend(bbox_to_anchor=(1.01, 1), loc="upper left")
    plt.ylabel("MEM [%]")
    plt.title(f"MEM Usage of {name}")
    plt.xticks(tick_pos, thread_stats)
    plt.grid()
    plt.savefig("./graphs/" + name + "_mem.png")
    plt.cla()
    plt.clf()
    plt.close()


def make_cpu_chart(name: str):
    ''' Makes a CPU utilisation chart and saves it to a file with "name_throughput.png"'''

    rs_name = "rs_" + name
    grpc_name = "grpc_" + name

    rs_df = pd.read_csv(
        f"../container-csv/{rs_name}.csv", delimiter=r"\s\s+", engine="python"
    )
    rs_df = rs_df[rs_df.NAME != "NAME"]
    rs_df["CPU %"] = percentage_to_float(rs_df["CPU %"])

    grpc_df = pd.read_csv(
        f"../container-csv/{grpc_name}.csv", delimiter=r"\s\s+", engine="python"
    )
    grpc_df = grpc_df[grpc_df.NAME != "NAME"]
    grpc_df["CPU %"] = percentage_to_float(grpc_df["CPU %"])

    fig, ax = plt.subplots(1, 1, figsize=(14, 7))

    sns.lineplot(
        x=rs_df.index,
        y="CPU %",
        color=RS_COLOR,
        data=rs_df,
        drawstyle="steps",
        label="RSOCKET",
        linewidth=3,
    )
    sns.lineplot(
        x=grpc_df.index,
        y="CPU %",
        color=GRPC_COLOR,
        data=grpc_df,
        drawstyle="steps",
        label="GRPC",
        linewidth=2,
    )

    thread_stats = THREAD_STATS
    if "sized" in name:
        thread_stats = SIZED_THREAD_STATS

    x_min, x_max = np.min(grpc_df.index), np.max(grpc_df.index)
    intervals = np.linspace(x_min, x_max, 5)
    tick_pos = [(s + e) / 2 for (s, e) in zip(intervals[0:-1], intervals[1:])]

    plt.legend(bbox_to_anchor=(1.01, 1), loc="upper left")
    plt.ylabel("CPU [%]")
    plt.title(f"CPU Usage of {name}")
    plt.xticks(tick_pos, thread_stats)
    plt.grid()
    plt.savefig("./graphs/" + name + "_cpu.png")
    plt.cla()
    plt.clf()
    plt.close()


def make_all_charts_for_each(names: list[str]):
    for name in names:
        make_all_charts(name)


def make_all_charts(name: str):
    make_throughput_chart(name)
    make_cpu_chart(name)
    make_mem_chart(name)


if __name__ == "__main__":
    make_all_charts_for_each(TEST_NAMES)
